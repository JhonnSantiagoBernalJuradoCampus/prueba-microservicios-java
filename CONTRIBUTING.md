# Contribución y Convenciones

## Flujo de ramas (Git Flow simplificado)
- Ramas principales: main (estable) y develop (integración)
- Ramas de trabajo:
  - Features: feature/<alcance-corto>
  - Fixes: fix/<alcance-corto>
  - Releases: release/v<version> (opcional)
  - Hotfixes: hotfix/<alcance-corto>
- Pull Requests:
  - feature/* → develop
  - develop → main para releases

## Convenciones de commit (Conventional Commits)
- feat(scope): descripción
- fix(scope): descripción
- chore(scope): descripción
- docs(scope): descripción
- refactor(scope): descripción
- test(scope): descripción

Ejemplos:
- feat(products): crear endpoint POST /products
- test(inventory): agregar unit test de service

## Estilo de código y formato
- Respetar .editorconfig
- Java 21, Spring Boot 3.x

## Revisión y checklist de PR
- [ ] Respuestas cumplen JSON:API
- [ ] Tests pasan (unit e integración) y cobertura no baja
- [ ] Swagger/OpenAPI actualizado
- [ ] Manejo de errores consistente
- [ ] README actualizado si aplica

