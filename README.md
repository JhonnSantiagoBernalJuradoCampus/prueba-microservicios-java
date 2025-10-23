# Prueba Técnica – Microservicios Products e Inventory (Java / Spring Boot)

## Arquitectura y decisiones
- Microservicios independientes: `products` y `inventory` (puertos 8081 y 8082).
- Estilo vertical por característica dentro de cada servicio: `features/<feature>/{api,application,domain}` + `infra/*`.
- Respuestas JSON:API (envoltorios `JsonApiData`, `JsonApiResponse`, manejo de errores centralizado).
- DTOs en controladores; servicios trabajan con entidades JPA. Mapeo con MapStruct.
- Base de datos: H2 en dev/test; PostgreSQL planificado para Docker/prod.
- Preferencia SQL vs NoSQL: modelado relacional (productos, inventario, compras); integridad y transacciones favorecen SQL. NoSQL no aporta ventaja clara aquí.

## Ejecución local
- Requiere Java 21.
- Perfiles: `dev` (por defecto) y `test`.
- Arranque:
  - Products: `cd services/products && .\mvnw.cmd -q spring-boot:run -Dspring-boot.run.profiles=dev`
  - Inventory: `cd services/inventory && .\mvnw.cmd -q spring-boot:run -Dspring-boot.run.profiles=dev`

## Endpoints y Swagger
- Products Swagger: `http://localhost:8081/swagger-ui/index.html`
- Inventory Swagger: `http://localhost:8082/swagger-ui/index.html`
- Actuator health: `http://localhost:8081/actuator/health`, `http://localhost:8082/actuator/health`

## Seguridad – API Key (sólo dev)
- Cabecera requerida en endpoints de negocio: `X-API-Key: dev-secret`.
- Whitelist (sin autenticación): `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`, `/actuator/**`, `/error`.
- Configuración por servicio:
  - `services/*/src/main/resources/application-dev.yml`:
    ```yaml
    security:
      api-key:
        enabled: true
        header: X-API-Key
        value: dev-secret
    ```
  - En `test` está deshabilitado (`enabled: false`).

## Contratos principales
- Products
  - POST `/products` → Crea producto (JSON:API)
  - GET `/products/{id}` → Obtiene por id
  - GET `/products` → Lista
- Inventory
  - PATCH `/inventory/{productId}` → Establece cantidad
  - GET `/inventory/{productId}` → Consulta cantidad

## Pruebas
- Unitarias: servicios y controladores (MockMvc standalone). Integración: flujo básico por servicio.
- Ejecutar (perfil test):
  - Products: `cd services/products && .\mvnw.cmd -q test -Dspring.profiles.active=test`
  - Inventory: `cd services/inventory && .\mvnw.cmd -q test -Dspring.profiles.active=test`

## Estándares
- JSON:API para respuestas/errores.
- DTOs + MapStruct: controladores sólo ven DTOs; servicios trabajan entidades.
- Validaciones con Bean Validation.



## Uso de IA
- Se usó IA para: scaffolding, diagnóstico de errores YAML/Swagger, diseño de estructura vertical, DTO/MapStruct, JSON:API, pruebas (MockMvc y SpringBootTest), y middleware API Key.
- Decisiones humanas: selección SQL vs NoSQL, vertical slice, estándares de respuesta y manejo de errores.



