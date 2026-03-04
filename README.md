# GeoEventos API

API REST para la plataforma **GeoEventos**, desarrollada con Spring Boot. Gestiona los eventos geolocalizados, el acceso a la base de datos PostgreSQL y la integración con ImgBB para el almacenamiento de imágenes.

> Este repositorio contiene únicamente el backend. Los clientes se encuentran en [GeoEventosGUI](https://github.com/AlfredoSWDev/GeoEventosGUI) y [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid).

---

## ¿Qué es GeoEventos?

GeoEventos es una plataforma que permite a empresas locales **publicar, promocionar y gestionar eventos** en un mapa interactivo, de forma similar a Google Maps. El modelo de negocio es **B2B (Business to Business)**: las empresas pagan por publicar sus eventos, mientras que los usuarios finales acceden a la información de forma totalmente gratuita.

El proyecto inicia con un **MVP CRUD** que centraliza toda la lógica de negocio en esta API, sentando las bases para la versión completa con mapa interactivo y despliegue en AWS.

---

## Stack Tecnológico

| Tecnología | Uso |
|------------|-----|
| Java 21 | Lenguaje principal |
| Spring Boot 3.3 | Framework principal |
| Spring Web | Controladores REST |
| Spring JDBC | Acceso a base de datos |
| Spring WebFlux | WebClient para llamadas a ImgBB |
| PostgreSQL | Base de datos |
| Jackson | Serialización / deserialización JSON |
| Gradle | Build system |

---

## Arquitectura

```
Clientes
  ├── GeoEventosGUI   (Swing / escritorio)
  ├── GeoEventosAndroid (Android / Kotlin)
  └── cualquier HTTP client
          │
          │  HTTP REST (JSON)
          ▼
┌──────────────────────────────┐
│        Controller Layer      │  ← Recibe y responde peticiones HTTP
├──────────────────────────────┤
│        Service Layer         │  ← Lógica de negocio y validaciones
├──────────────────────────────┤
│       Repository Layer       │  ← Acceso a datos con JdbcTemplate
└──────────────────────────────┘
          │                │
          ▼                ▼
     PostgreSQL         ImgBB API
```

### Estructura del Proyecto

```
src/main/java/com/alfredo/api/
├── GeoEventosApiApplication.java    # Punto de entrada
├── controller/
│   ├── EventoController.java        # Endpoints CRUD de eventos
│   └── ImagenController.java        # Endpoint de subida de imágenes
├── service/
│   ├── EventoService.java           # Lógica de negocio
│   └── ImagenService.java           # Integración con ImgBB
├── repository/
│   └── EventoRepository.java        # Consultas SQL con JdbcTemplate
├── model/
│   └── Evento.java                  # Entidad de dominio
└── dto/
    ├── EventoDTO.java               # Datos de entrada (crear/actualizar)
    └── ImagenResponseDTO.java       # Respuesta tras subir imagen
```

---

## Endpoints

### Eventos

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `GET` | `/api/eventos` | Listar todos los eventos | — |
| `GET` | `/api/eventos?q={texto}` | Buscar por nombre o lugar | — |
| `GET` | `/api/eventos/{id}` | Obtener evento por ID | — |
| `POST` | `/api/eventos` | Crear nuevo evento | `EventoDTO` JSON |
| `PUT` | `/api/eventos/{id}` | Actualizar evento | `EventoDTO` JSON |
| `DELETE` | `/api/eventos/{id}` | Eliminar evento | — |

### Imágenes

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `POST` | `/api/imagenes/subir` | Subir imagen a ImgBB | `multipart/form-data` |

---

## Ejemplos de Uso

**Listar eventos:**
```bash
curl http://localhost:8080/api/eventos
```

**Crear evento:**
```bash
curl -X POST http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -d '{
    "nombreEvento":      "Festival de Verano",
    "valorEvento":       "USD 15",
    "lugarEvento":       "Plaza Central",
    "vigenciaEvento":    "Vigente",
    "descripcionEvento": "Concierto al aire libre",
    "fotosEvento":       null,
    "latitud":           -33.4489,
    "longitud":          -70.6693
  }'
```

**Subir imagen:**
```bash
curl -X POST http://localhost:8080/api/imagenes/subir \
  -F "archivo=@/ruta/imagen.jpg"
```

**Respuesta de subida de imagen:**
```json
{
    "success": true,
    "url": "https://i.ibb.co/xxxxx/imagen.jpg",
    "mensaje": "Imagen subida correctamente"
}
```

---

## Base de Datos

### Esquema

```sql
CREATE TABLE public.eventos (
    event_id           SERIAL PRIMARY KEY,
    nombre_evento      TEXT NOT NULL,
    fotos_evento       TEXT,
    descripcion_evento TEXT,
    vigencia_evento    TEXT,
    valor_evento       TEXT,
    lugar_evento       TEXT NOT NULL,
    latitud            DOUBLE PRECISION,
    longitud           DOUBLE PRECISION
);
```

---

## Requisitos

- Java 21+
- PostgreSQL corriendo localmente (o en la nube)
- API Key de [ImgBB](https://imgbb.com)

---

## Configuración

Edita `src/main/resources/application.properties`:

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

---

## Cómo ejecutar

1. Clona este repositorio:
   ```bash
   git clone https://github.com/AlfredoSWDev/GeoEventosAPI.git
   ```
2. Configura `application.properties` con tus credenciales.
3. Asegúrate de que PostgreSQL esté corriendo y la base de datos `geoeventos` exista.
4. Ejecuta con Gradle:
   ```bash
   ./gradlew bootRun
   ```
5. La API estará disponible en `http://localhost:8080`.

---

## Códigos de Respuesta

| Código | Significado |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `201 Created` | Evento creado correctamente |
| `204 No Content` | Evento eliminado correctamente |
| `404 Not Found` | Evento no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

## Roadmap

- [x] Integración con coordenadas geográficas (latitud / longitud)
- [ ] Endpoint de eventos por proximidad geográfica
- [ ] Autenticación y autorización (JWT)
- [ ] Paginación en listado de eventos
- [ ] Despliegue en AWS (EC2 + RDS)
- [ ] Documentación automática con Swagger / OpenAPI

---

## Repositorios del Proyecto

| Repositorio | Descripción |
|-------------|-------------|
| [GeoEventosGUI](https://github.com/AlfredoSWDev/GeoEventosGUI) | Cliente de escritorio Swing |
| [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid) | Cliente móvil Android |
| **GeoEventosAPI** | Este repositorio — API REST Spring Boot |