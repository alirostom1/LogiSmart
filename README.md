# Smart Delivery Management System

Spring-based REST API for managing couriers and deliveries.

---

## Features
- Courier CRUD operations
- Delivery tracking with status updates
- RESTful JSON API
- Data validation and error handling

---

## API Endpoints

### Postman

 - [click here](https://www.postman.com/voxa-team/workspace/public-collections/collection/42850483-bd78f181-4598-41c6-a208-66f4319497c7?action=share&creator=42850483)

### Couriers
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/couriers` | List all couriers |
| GET | `/api/couriers/{id}` | Get courier by ID |
| POST | `/api/couriers` | Create courier |
| PUT | `/api/couriers/{id}` | Full update |
| DELETE | `/api/couriers/{id}` | Delete courier |

### Deliveries
| Method | Endpoint                       | Description                  |
|--------|--------------------------------|------------------------------|
| GET | `/api/deliveries`              | List all deliveries          |
| GET | `/api/deliveries/{id}`         | Get delivery by ID           |
| GET | `/api/deliveries/courier/{id}` | Get deliveries by courier ID |
| POST | `/api/deliveries`              | Create delivery              |
| PUT | `/api/deliveries/{id}`         | Full update                  |
| PATCH | `/api/deliveries/{id}/status`  | Update status                |
| DELETE | `/api/deliveries/{id}`         | Delete delivery              |

---

## Status Flow
`PREPARATION` → `IN_TRANSIT` → `DELIVERED`

---

## Tech Stack
- Spring Core
- Spring Data JPA
- Spring MVC
- PostgreSQL
- Maven
- Embedded Tomcat
---

## UML Class diagram

![class diagram](https://github.com/alirostom1/LogiSmart/blob/main/docs/LogiSmart.png)

---
## Run

```bash
    git clone https://github.com/alirostom1/LogiSmart.git
    cd api
    cp src/main/resources/template.properties src/main/resources/database.properties
```
### Edit database.properties with your PostgreSQL credentials

```shell
    mvn clean compile
    mvn exec:java -Dexec.mainClass='io.github.alirostom1.logismart.Main'
```
---