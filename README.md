# GeoEventosAPI

GeoEventosAPI es un módulo de la aplicación GeoEventos que proporciona una interfaz para el manejo de eventos y imágenes. Este módulo es parte del proyecto GeoEventos, que tiene como objetivo crear un sistema de gestión de eventos integral.

## Visión general

El API tiene dos endpoints principales: `eventos` y `imagenes`. El endpoint `eventos` permite listar, crear, leer, actualizar y eliminar eventos. El endpoint `imagenes` permite subir imágenes a ImgBB.

## Detalles técnicos

### Dependencias

* Java 25+
* Spring Boot 4.3.10
* PostgreSQL 13.2
* ImgBB API
* Maven 3.6.3

### Configuración

Edita `src/main/resources/application.properties` para configurar el API:

```properties
# Servidor
server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/geoeventos
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# ImgBB
imgbb.api.key=TU_API_KEY
imgbb.api.url=https://api.imgbb.com/1/upload

# Logs (desarrollo)
logging.level.org.springframework.jdbc=DEBUG
```

### Endpoints

| Endpoint | Method | Description | Body |
|----------|--------|-------------|------|
| /api/eventos | GET | Listar todos los eventos | — |
| /api/eventos | POST | Crear nuevo evento | `EventoDTO` JSON |
| /api/eventos/{id} | GET | Obtener evento por ID | — |
| /api/eventos/{id} | PUT | Actualizar evento | `EventoDTO` JSON |
| /api/eventos/{id} | DELETE | Eliminar evento | — |
| /api/imagenes/subir | POST | Subir imagen a ImgBB | `multipart/form-data` |

### Códigos de respuesta

| Código | Significado |
|------|-------------|
| 200 OK | Operación exitosa |
| 201 Created | Evento creado correctamente |
| 204 No Content | Evento eliminado correctamente |
| 404 Not Found | Evento no encontrado |
| 500 Internal Server Error | Error interno del servidor |

## Roadmap

* [x] Integración con coordenadas geográficas (latitud / longitud)
* [x] Unit tests con JUnit 6 + Mockito (46 tests, 3 capas)
* [ ] Endpoint de eventos por proximidad geográfica
* [ ] Autenticación y autorización (JWT)
* [ ] Paginación en listado de eventos
* [ ] Despliegue en AWS (EC2 + RDS)
* [ ] Documentación automática con Swagger / OpenAPI


