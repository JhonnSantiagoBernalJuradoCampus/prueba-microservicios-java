# Catálogo y inventario — microservicios en Java / Spring Boot

Sistema de catálogo de productos e inventario con un flujo de compra orquestado entre dos microservicios independientes. Cada servicio tiene su propia base de datos, API REST en JSON:API, autenticación por API Key y despliegue reproducible con Docker.

Es un proyecto de backend pensado para mostrar arquitectura de microservicios, límites de dominio claros y patrones de diseño aplicados en Java 21 y Spring Boot 3.

## Stack

| Área | Tecnología |
| --- | --- |
| Lenguaje y runtime | Java 21 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Validation, Actuator) |
| Persistencia | PostgreSQL 16 (H2 en tests) |
| Mapeo | MapStruct, Lombok |
| API | JSON:API, OpenAPI / Swagger (springdoc) |
| Comunicación | HTTP con `RestClient` |
| Contenedores | Docker, Docker Compose (imágenes multi-stage Maven + JRE) |

## Arquitectura

Dos servicios con **database per service**: comparten la misma instancia de PostgreSQL en Docker, pero bases distintas (`products_db` e `inventory_db`).

```
Cliente  →  products (8081)  →  inventory (8082)
                │                      │
          products_db            inventory_db
```

- **products**: catálogo y caso de uso de compra. Valida que el producto exista, llama a inventario para descontar stock de forma atómica y responde con el total.
- **inventory**: estado de stock. Solo operaciones de consulta, asignación y decremento; no orquesta negocio de compra.

La compra vive en `products` para mantener bajo acoplamiento: inventario no conoce precios ni el flujo comercial. Si el stock no alcanza, `inventory` responde 400 y `products` propaga el error JSON:API sin reinterpretarlo.

![Diagrama de Arquitectura](docs/Arquitectura.png)

![Diagrama del Flujo de Compra](docs/FlujoCompra.png)

### Organización del código

Cada microservicio sigue capas por feature (`features/<dominio>`) e infraestructura transversal (`infra`):

- **api**: controladores, DTOs y contratos HTTP.
- **application**: casos de uso (`ProductService`, `PurchaseService`, `InventoryService`).
- **domain**: entidades y repositorios.
- **infra**: seguridad, JSON:API, cliente HTTP, manejo global de errores.

## Patrones de diseño

| Patrón | Dónde se aplica |
| --- | --- |
| Arquitectura por capas + empaquetado por feature | `api` / `application` / `domain` / `infra` en cada servicio |
| Database per service | Bases `products_db` e `inventory_db` |
| Repository | Spring Data JPA (`ProductRepository`, `InventoryRepository`) |
| DTO + Mapper | DTOs de API y MapStruct (`ProductMapper`, `InventoryMapper`) |
| Service / Application service | Casos de uso transaccionales; `PurchaseService` orquesta producto + inventario |
| API Gateway / Client (lado consumidor) | `InventoryClient` encapsula llamadas HTTP a inventario |
| Dependency Injection | Inyección por constructor en servicios, controladores y cliente |
| Filter (cadena de filtros servlet) | `ApiKeyFilter` (`OncePerRequestFilter`) valida `X-API-Key` |
| Controller Advice | `GlobalExceptionHandler` unifica errores en JSON:API |
| Configuration | Beans de `RestClient` con interceptor de API Key y timeouts implícitos de Spring |

También se usan convenciones de Spring (perfiles `dev` / `prod` / `test`) y builds Docker multi-stage (compilación Maven + runtime JRE).

## Contratos HTTP y errores

Las respuestas siguen JSON:API (`data` / `errors`).

| Código | Cuándo |
| --- | --- |
| 404 | Producto inexistente (validado en `products`) |
| 400 | Stock insuficiente (emitido por `inventory` y reenviado por `products`) |
| 422 | Validación de payload |
| 401 | API Key ausente o inválida |

No se reintentan compras automáticamente: sin idempotencia, un reintento podría descontar stock dos veces.

## Requisitos

- Docker y Docker Compose v2

## Instalación y ejecución

```bash
git clone https://github.com/bernalSantiago1/microservicios-java.git
cd microservicios-java

docker compose build
docker compose up -d
docker compose ps
```

## Swagger

- Products: http://localhost:8081/swagger-ui/index.html
- Inventory: http://localhost:8082/swagger-ui/index.html

Cabecera de autenticación: `X-API-Key: prod-secret`

### Endpoints

**Products**

- `POST /products` — crear producto
- `GET /products/{id}` — obtener producto
- `GET /products` — listar productos
- `POST /purchase` — comprar (orquesta inventario)

**Inventory**

- `GET /inventory/{productId}` — consultar cantidad
- `PATCH /inventory/{productId}` — establecer cantidad (cuerpo JSON:API)
- `PATCH /inventory/{productId}/decrement?amount={n}` — decrementar

**Health**

- http://localhost:8081/actuator/health
- http://localhost:8082/actuator/health

## Variables de entorno

Definidas en `docker-compose.yml` y leídas por los perfiles `prod`:

- Ambos servicios: `SPRING_PROFILES_ACTIVE`, `DB_USERNAME`, `DB_PASSWORD`, `API_KEY_ENABLED`, `API_KEY_HEADER`, `API_KEY_VALUE`
- `products` (opcional, con valores por defecto en `application-prod.yml`): `INVENTORY_BASE_URL`, `INVENTORY_API_KEY_HEADER`, `INVENTORY_API_KEY_VALUE`

## Tests

Unitarios e de integración por microservicio (perfil `test`, H2). Desde la raíz del repo, con Docker:

```bash
docker compose run --rm tests
```

O con Maven local (Java 21):

```bash
mvn -B -Dspring.profiles.active=test -f services/products/pom.xml test
mvn -B -Dspring.profiles.active=test -f services/inventory/pom.xml test
```

## Estructura del repositorio

```
├── docker-compose.yml
├── docker/postgres/initdb/   # crea products_db e inventory_db
├── docs/                     # diagramas de arquitectura y flujo de compra
├── services/products/
└── services/inventory/
```
