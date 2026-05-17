# ms_admision

Microservicio de admisión de paquetes del sistema **SIVEBO** (Sistema de Gestión de Envíos y Bodega).

---

## Descripción

Gestiona el ingreso de paquetes al sistema, genera el código de tracking inicial y registra el estado del paquete desde su recepción. Cada admisión queda asociada a un cliente registrado en `ms_clientes`.

---

## Tecnologías

- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- MariaDB
- Lombok
- Maven

---

## Base de datos

```
db_ms_admision
```

---

## Configuración

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/db_ms_admision?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
server.port=8087
```

---

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/admisiones` | Registra el ingreso de un paquete |
| GET | `/admisiones` | Lista todas las admisiones |
| GET | `/admisiones/{id}` | Obtiene una admisión por ID |

### Ejemplo — Registrar admisión

```json
POST /admisiones
{
  "clienteId": 1,
  "descripcionPaquete": "Caja electrónica frágil",
  "peso": 2.5,
  "dimensiones": "30x20x15 cm",
  "direccionDestino": "Av. Siempreviva 742, Santiago"
}
```

### Respuesta

```json
{
  "id": 1,
  "clienteId": 1,
  "paqueteId": 1,
  "descripcionPaquete": "Caja electrónica frágil",
  "peso": 2.5,
  "dimensiones": "30x20x15 cm",
  "direccionDestino": "Av. Siempreviva 742, Santiago",
  "codigoTracking": "SIVEBO-A3F8C2B1",
  "estadoActual": "INGRESADO",
  "fechaIngreso": "2025-05-17T10:30:00"
}
```

---

## Estados de tracking

| Estado | Descripción |
|--------|-------------|
| `INGRESADO` | Paquete recibido en el sistema |
| `EN_BODEGA` | En proceso de clasificación |
| `EN_TRANSITO` | En camino al destino |
| `EN_REPARTO` | En reparto local |
| `ENTREGADO` | Entregado al destinatario |
| `DEVUELTO` | Devuelto al remitente |

---

## Estructura del proyecto

```
src/main/java/com/sivebo/ms_admision/
├── config/
│   └── GlobalExceptionHandler.java
├── controller/
│   └── AdmisionController.java
├── dto/
│   ├── AdmisionRequest.java
│   └── AdmisionResponse.java
├── model/
│   ├── Admision.java
│   ├── EstadoTracking.java
│   └── Paquete.java
├── repository/
│   ├── AdmisionRepository.java
│   └── PaqueteRepository.java
└── service/
    └── AdmisionService.java
```

---

## Ejecución

```bash
./mvnw spring-boot:run
```

---

## Notas

- El `clienteId` hace referencia al ID del cliente en `ms_clientes`. No existe una FK real entre microservicios — la validación de existencia del cliente se implementará vía WebClient en una iteración futura.
- El código de tracking se genera automáticamente con el formato `SIVEBO-XXXXXXXX`.
- La base de datos se crea automáticamente si no existe.
