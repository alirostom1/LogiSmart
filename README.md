# Smart Delivery Management System — Version 2

Spring-based REST API for managing couriers, deliveries, senders, recipients, products, and zones.

---

## Features

- Courier, Delivery, Zone, Person (Sender/Recipient), and Product CRUD operations
- Advanced delivery tracking with status updates and assignments
- Paginated JSON API responses
- Data validation and robust error handling
- OpenAPI/Swagger documentation at `/swagger-ui/index.html` for all endpoints

---

## API Endpoints

### Postman

- [Public Postman Collection](https://www.postman.com/voxa-team/workspace/public-collections/collection/42850483-bd78f181-4598-41c6-a208-66f4319497c7?action=share&creator=42850483)

### Couriers

| Method | Endpoint                                       | Description                   |
|--------|------------------------------------------------|-------------------------------|
| GET    | `/api/v2/couriers`                             | List all couriers (paginated) |
| GET    | `/api/v2/couriers/{id}`                        | Get courier by ID             |
| GET    | `/api/v2/couriers/{id}/with-deliveries`        | Get courier with deliveries   |
| GET    | `/api/v2/couriers/zone/{zoneId}`               | Couriers by zone (paginated)  |
| POST   | `/api/v2/couriers`                             | Create courier                |
| PUT    | `/api/v2/couriers/{id}`                        | Update courier                |
| DELETE | `/api/v2/couriers/{id}`                        | Delete courier                |

### Deliveries

| Method | Endpoint                                                | Description                                |
|--------|---------------------------------------------------------|--------------------------------------------|
| GET    | `/api/v2/deliveries`                                   | List all deliveries (paginated, search)    |
| GET    | `/api/v2/deliveries/{id}`                              | Get delivery by ID                         |
| GET    | `/api/v2/deliveries/{id}/tracking`                     | Track delivery                             |
| GET    | `/api/v2/deliveries/courier/{courierId}`               | Deliveries by courier (paginated)          |
| GET    | `/api/v2/deliveries/sender/{senderId}`                 | Deliveries by sender (paginated)           |
| GET    | `/api/v2/deliveries/recipient/{recipientId}`           | Deliveries by recipient (paginated)        |
| GET    | `/api/v2/deliveries/search`                            | Search deliveries (paginated, request body)|
| POST   | `/api/v2/deliveries`                                   | Create delivery                            |
| PATCH  | `/api/v2/deliveries/{id}/assign-collecting-courier`    | Assign collecting courier                  |
| PATCH  | `/api/v2/deliveries/{id}/assign-shipping-courier`      | Assign shipping courier                    |
| PATCH  | `/api/v2/deliveries/{id}/status`                       | Update delivery status                     |
| DELETE | `/api/v2/deliveries/{id}`                              | Delete delivery                            |

### Zones

| Method | Endpoint                  | Description                         |
|--------|---------------------------|-------------------------------------|
| GET    | `/api/v2/zones`           | List all zones (paginated)          |
| GET    | `/api/v2/zones/{id}`      | Get zone by ID                      |
| POST   | `/api/v2/zones`           | Create zone                         |
| PUT    | `/api/v2/zones/{id}`      | Update zone                         |
| DELETE | `/api/v2/zones/{id}`      | Delete zone                         |

### People (Sender & Recipient)

| Method | Endpoint                          | Description                          |
|--------|-----------------------------------|--------------------------------------|
| GET    | `/api/v2/persons/senders`         | List all senders (paginated)         |
| GET    | `/api/v2/persons/recipients`      | List all recipients (paginated)      |
| GET    | `/api/v2/persons/sender/{id}`     | Get sender by ID                     |
| GET    | `/api/v2/persons/recipient/{id}`  | Get recipient by ID                  |
| POST   | `/api/v2/persons/sender`          | Create new sender                    |
| POST   | `/api/v2/persons/recipient`       | Create new recipient                 |
| PUT    | `/api/v2/persons/sender/{id}`     | Update sender                        |
| PUT    | `/api/v2/persons/recipient/{id}`  | Update recipient                     |
| DELETE | `/api/v2/persons/sender/{id}`     | Delete sender                        |
| DELETE | `/api/v2/persons/recipient/{id}`  | Delete recipient                     |

### Products

| Method | Endpoint                     | Description                    |
|--------|------------------------------|--------------------------------|
| GET    | `/api/v2/products`           | List all products (paginated)  |
| GET    | `/api/v2/products/{id}`      | Get product by ID              |
| POST   | `/api/v2/products`           | Create product                 |
| PUT    | `/api/v2/products/{id}`      | Update product                 |
| DELETE | `/api/v2/products/{id}`      | Delete product                 |

---

## Status Flow

`CREATED` → `COLLECTED` → `IN_STOCK` → `IN_TRANSIT` → `DELIVERED`

---

## Tech Stack

- Spring Boot Core
- Spring Data JPA
- Spring MVC & Validation
- PostgreSQL
- Maven
- Embedded Tomcat
- Springdoc OpenAPI for API docs

---

## UML Class Diagram

![class diagram](https://github.com/alirostom1/LogiSmart/blob/v2.0/docs/V2/LogiSmart.png)

---

## Run Locally

```bash
    git clone https://github.com/alirostom1/LogiSmart.git
    cd api
    cp .env.example .env
```

### Edit .env with your PostgreSQL credentials

```shell
    mvn clean compile
    mvn exec:java -Dexec.mainClass='io.github.alirostom1.logismart.Main'
```
---

## API Documentation

Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) for complete interactive docs.

---
## Test Coverage & Code Quality

### JaCoCo Code Coverage

After running your tests with Maven, a JaCoCo coverage report is generated at `target/site/jacoco/index.html`.

![JaCoCo Coverage](https://github.com/alirostom1/LogiSmart/blob/tests/docs/Jacoco.png)

---

### SonarQube Analysis

SonarQube checks static code quality and technical debt.

![SonarQube Coverage](https://github.com/alirostom1/LogiSmart/blob/tests/docs/Jacoco.png)

To run SonarQube locally:

1. Copy the SonarQube properties file:

   cp sonar-project.properties.example sonar-project.properties

2. Run the SonarQube scanner (ensure your SonarQube instance is running):

   sonar-scanner

This will analyze your project and you can view the results on your SonarQube dashboard.
