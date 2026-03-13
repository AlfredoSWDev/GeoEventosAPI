# GeoEventosAPI

GeoEventosAPI es un módulo de la aplicación GeoEventos que proporciona una interfaz para el manejo de eventos y imágenes. Este módulo es parte del proyecto GeoEventos, que tiene como objetivo crear un sistema de gestión de eventos integral.

## URL de producción

```
https://geoeventosapi.onrender.com
```

> ℹ️ El servicio usa el plan gratuito de Render. La primera petición puede tardar ~30 segundos en despertar si estuvo inactivo.

## Visión general

El API tiene dos endpoints principales: `eventos` e `imagenes`. El endpoint `eventos` permite listar, crear, leer, actualizar y eliminar eventos. El endpoint `imagenes` permite subir imágenes a ImgBB.

## Detalles técnicos

### Stack

* Java 21 (despliegue) / Java 25 (desarrollo local)
* Spring Boot 4.0.3
* PostgreSQL en [Neon](https://neon.tech)
* ImgBB API
* Gradle 9.3.1
* Docker (para despliegue)

### Configuración local

Crea un archivo `.env` en la raíz del proyecto (ver `.env.example`):

```env
DB_URL=jdbc:postgresql://TU_HOST.neon.tech/geoeventos?sslmode=require&channel_binding=require
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
SERVER_PORT=8080
IMGBB_API_KEY=tu_api_key
IMGBB_URL=https://api.imgbb.com/1/upload
```

Luego ejecuta:

```bash
./gradlew bootRun
```

### Endpoints

| Endpoint | Method | Description | Body |
|----------|--------|-------------|------|
| /api/eventos | GET | Listar todos los eventos | — |
| /api/eventos?q={texto} | GET | Buscar eventos por nombre o lugar | — |
| /api/eventos | POST | Crear nuevo evento | `EventoDTO` JSON |
| /api/eventos/{id} | GET | Obtener evento por ID | — |
| /api/eventos/{id} | PUT | Actualizar evento | `EventoDTO` JSON |
| /api/eventos/{id} | DELETE | Eliminar evento | — |
| /api/imagenes/subir | POST | Subir imagen a ImgBB | `multipart/form-data` |

### Códigos de respuesta

| Código | Significado |
|--------|-------------|
| 200 OK | Operación exitosa |
| 201 Created | Evento creado correctamente |
| 204 No Content | Evento eliminado correctamente |
| 404 Not Found | Evento no encontrado |
| 500 Internal Server Error | Error interno del servidor |

## Tests

46 tests unitarios con JUnit 5 + Mockito cubriendo 3 capas: controller, service y repository.

```bash
./gradlew test
```

## Roadmap

* [x] Integración con coordenadas geográficas (latitud / longitud)
* [x] Unit tests con JUnit 5 + Mockito (46 tests, 3 capas)
* [x] Despliegue en Render + Neon
* [ ] Endpoint de eventos por proximidad geográfica
* [ ] Autenticación y autorización (JWT)
* [ ] Paginación en listado de eventos
* [ ] Documentación automática con Swagger / OpenAPI

## Parte del ecosistema GeoEventos

| Repositorio           | Descripción                              |
|-----------------------|------------------------------------------|
| [GeoEventosAPI]()     | Spring Boot 4 + Java 21 + PostgreSQL ← aquí |
| [GeoEventosWeb]()     | Kotlin Multiplatform / Compose for Web   |
| [GeoEventosAndroid]() | Kotlin + Jetpack Compose + OSMDroid     |
| [GeoEventosDesktop]() | Java 23 + Swing + JavaFX                 |
| [GeoEventosDB]()      | Schema y migraciones PostgreSQL          |