# GeoEventosAPI

Backend del ecosistema **GeoEventos** — plataforma B2B de gestión de eventos geolocalizados.

API REST construida con **Spring Boot 4.0.3 + Java 21**, desplegada en Render con base de datos PostgreSQL en Neon.

🔗 **URL de producción:** `https://geoeventosapi.onrender.com`

> ℹ️ El servicio usa el plan gratuito de Render. La primera petición puede tardar ~30 segundos en despertar si estuvo inactivo.

---

## Stack

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 21 (producción) / Java 25 (desarrollo local) |
| Framework | Spring Boot 4.0.3 |
| Base de datos | PostgreSQL en [Neon](https://neon.tech) |
| Imágenes | ImgBB API |
| Build | Gradle 9.3.1 |
| Deploy | Docker → Render |
| Tests | JUnit 5 + Mockito (46 tests) |

---

## Endpoints

| Endpoint | Método | Descripción | Body |
|----------|--------|-------------|------|
| `/api/eventos` | GET | Listar todos los eventos | — |
| `/api/eventos?q={texto}` | GET | Buscar por nombre o lugar | — |
| `/api/eventos` | POST | Crear nuevo evento | `EventoDTO` JSON |
| `/api/eventos/{id}` | GET | Obtener evento por ID | — |
| `/api/eventos/{id}` | PUT | Actualizar evento | `EventoDTO` JSON |
| `/api/eventos/{id}` | DELETE | Eliminar evento | — |
| `/api/imagenes/subir` | POST | Subir imagen a ImgBB | `multipart/form-data` |

### Códigos de respuesta

| Código | Significado |
|--------|-------------|
| 200 OK | Operación exitosa |
| 201 Created | Evento creado |
| 204 No Content | Evento eliminado |
| 404 Not Found | Evento no encontrado |
| 500 Internal Server Error | Error interno |

---

## Configuración local

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

---

## Tests

46 tests unitarios cubriendo 3 capas: controller, service y repository.

```bash
./gradlew test
```

---

## Roadmap

- [x] CRUD completo de eventos
- [x] Coordenadas geográficas (latitud / longitud)
- [x] Endpoint de imágenes (ImgBB)
- [x] 46 tests unitarios (JUnit 5 + Mockito)
- [x] Despliegue en Render + Neon
- [ ] Endpoint de eventos por proximidad geográfica
- [ ] Autenticación y autorización (JWT)
- [ ] Paginación en listado de eventos
- [ ] Documentación automática con Swagger / OpenAPI

---

## Parte del ecosistema GeoEventos

| Repositorio | Descripción |
|-------------|-------------|
| **GeoEventosAPI** | Spring Boot 4 + Java 21 + PostgreSQL ← aquí |
| [GeoEventosWeb](https://github.com/AlfredoSWDev/GeoEventosWeb) | Kotlin/Wasm + Compose for Web |
| [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid) | Kotlin + Jetpack Compose + OSMDroid |
| [GeoEventosDesktop](https://github.com/AlfredoSWDev/GeoEventosDesktop) | Java 23 + Swing + JavaFX |
| [GeoEventosDB](https://github.com/AlfredoSWDev/GeoEventosDB) | Schema y migraciones PostgreSQL |

---

## Autor

**Alfredo Sanchez** — [@AlfredoSWDev](https://github.com/AlfredoSWDev)

📺 Stream de desarrollo en [Twitch](https://twitch.tv/AlfredoSWDev) · [YouTube](https://youtube.com/@AlfredoSWDev)
