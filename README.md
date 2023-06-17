# Periodicals - Spring Boot Microservices

  The *Administrator* is responsible for filling the catalog of periodicals.\
  The *Reader* can subscribe to the periodical by first selecting a *Publication* from the list.\
  The system calculates the amount to be paid and registers the *Payment*.

## Steps to Setup

**1. Clone the repository**

  ```bash
  git clone https://github.com/pmrochko/periodicals-microservices.git
  ```

**2. Create local or docker database for each service**

   | Database | Table / Collection | 
   | -------- | ------------------ |
   | PostgreSQL (name: *user_service_db*) | *User* table |
   | PostgreSQL (name: *payment_service_db*) | *Payment* table |
   | MongoDB (name: *catalog_service_db*) | *Topic* and *Publication* collections|

**3. Change username, password, url for each database in config files**

+ open `src/main/resources/application.yml` or `src/main/resources/application-docker.yml` (if using docker compose) by going inside each service folder
+ change and `spring:datasource:username` and `spring:datasource:password` as per your db installation

### Run the application locally

  - Run `mvn clean verify` by going inside each folder to build the applications.
  - After that run `mvn spring-boot:run` by going inside each folder to start the applications.
  - The app will start running at <http://localhost:8080>

### Run the application using Docker

  - Run `docker-compose up -d` to start the applications.
  - The app will start running at <http://localhost:8181>

## Explore Rest APIs

The app defines following CRUD APIs.

### Eureka Discovery Server

| Method | Url | Description |
| ------ | --- | ----------- |
| GET    | /api/v1/eureka | Go to Eureka page |

### Users

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/users | Get all users | |
| GET    | /api/v1/users/{userId} | Get user by id | |
| POST   | /api/v1/users | Create a new user | [JSON](#usercreate) |
| PUT    | /api/v1/users/{userId} | Update a user | [JSON](#userupdate) |

### Payments

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| POST   | /api/v1/payments | Create new payment | [JSON](#paymentcreate) |
| GET    | /api/v1/payments?userId= | Get a list of payments by user id | |

### Catalog [Topics]
  
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/topics | Get all topics | |
| POST   | /api/v1/topics | Create new topic | [JSON](#topiccreate) |
| PUT    | /api/v1/topics/{topicId} | Update a topic | [JSON](#topicupdate) |
| DELETE | /api/v1/topics/{topicId} | Delete a topic | |

### Catalog [Publications]
  
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/topics/{topicName}/publications | Get all publications by topic name | |
| GET    | /api/v1/publications/{publicationId} | Get a publication by id | |
| POST   | /api/v1/topics/{topicName}/publications | Create new publication | [JSON](#publicationcreate) |
| PUT    | /api/v1/publications/{publicationId}| Update a publication | [JSON](#publicationupdate) |
| DELETE | /api/v1/publications/{publicationId} | Delete a publication | |
