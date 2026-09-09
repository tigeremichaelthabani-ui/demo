# Spring Boot Project Learning

This document contains my notes and practical learning from building a Spring Boot application connected to an H2 database.

The project covers:

- Spring Boot project configuration
- Maven dependencies and `pom.xml`
- H2 database configuration
- Spring Boot auto-configuration
- Manual DataSource configuration
- JPA and Hibernate
- REST endpoints
- Entity classes
- Repository layer
- Service layer
- Service interfaces
- Controller layer
- Database CRUD operations

---

# 1. DataSource Configuration

## 1.1 Adding a Database Dependency

The first step in connecting a Spring Boot application to a database is adding the required database dependency.

For this project, I am using the **H2 Database**.

The H2 dependency is added to the `pom.xml` file:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### What does `<scope>` mean?

Maven dependency scope determines when and where a dependency is available during the project lifecycle.

Common scopes include:

| Scope | Purpose |
|---|---|
| `compile` | Available during compilation, testing and runtime |
| `test` | Available only during testing |
| `runtime` | Required at runtime but not for compiling the application |
| Not specified | Defaults to `compile` |

For the H2 database dependency:

```xml
<scope>runtime</scope>
```

means that H2 is required when the application is running.

---

# 2. Adding Database Configuration

There are two approaches that I have explored for configuring the database:

1. **Auto-configuration**
2. **Manual configuration using a `@Configuration` class**

---

## 2.1 Auto-Configuration

Spring Boot can automatically configure the DataSource when the appropriate dependencies and configuration properties are provided.

The database configuration is placed in:

```text
src/main/resources/application.properties
```

### Application Configuration

```properties
spring.application.name=demo
```

### Database Connection

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
```

### H2 Console Settings

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### JPA Settings

The following settings can be used to allow Hibernate to create or update database tables based on JPA entities:

```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

> **Note:** The exact JPA configuration required depends on how the application is being configured and which Spring Boot version and dependencies are being used.

---

## 2.2 Manual DataSource Configuration

The second approach is to manually configure the DataSource using a Java configuration class.

The class is annotated with:

```java
@Configuration
```

Example:

```java
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .driverClassName(driverClassName)
                .build();
    }
}
```

### What is happening here?

The configuration class:

1. Reads database properties from `application.properties`.
2. Creates a `DataSource`.
3. Registers the `DataSource` as a Spring Bean using `@Bean`.
4. Allows other components of the application to inject and use the DataSource.

---

# 3. Maven `pom.xml`

The `pom.xml` file is the main configuration file for a Maven project.

It contains information about:

- Project identity
- Dependencies
- Dependency management
- Plugins
- Build configuration
- Parent configuration

---

## 3.1 `<parent>`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.0</version>
    <relativePath/>
</parent>
```

### What does the parent do?

The Spring Boot parent provides configuration and dependency management for the project.

It provides sensible defaults such as:

- Compatible dependency versions
- Maven plugin configuration
- Java compiler configuration
- Spring Boot build and packaging support

Without the Spring Boot parent, many dependency versions would need to be managed manually.

For example, with dependency management:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Without dependency management, a version may need to be specified:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>...</version>
</dependency>
```

### Essential?

**Recommended for most Spring Boot projects.**

---

## 3.2 `<groupId>`

Example:

```xml
<groupId>h2demo.example</groupId>
```

The `groupId` identifies the organization, group or namespace associated with the project.

Examples:

```xml
<groupId>com.google</groupId>
<groupId>org.springframework</groupId>
<groupId>com.mycompany</groupId>
```

For this project:

```xml
<groupId>h2demo.example</groupId>
```

The Java package is:

```java
h2demo.example.demo
```

### Purpose

It helps uniquely identify the project within the Maven ecosystem.

### Essential?

**Yes.**

---

## 3.3 `<artifactId>`

Example:

```xml
<artifactId>demo</artifactId>
```

The `artifactId` identifies the actual application artifact.

For example:

```bash
mvn clean package
```

may produce:

```text
demo-0.0.1-SNAPSHOT.jar
```

because:

```text
artifactId = demo
version    = 0.0.1-SNAPSHOT
```

The artifact name could be changed to:

```xml
<artifactId>student-management-api</artifactId>
```

which could produce:

```text
student-management-api-0.0.1-SNAPSHOT.jar
```

### Essential?

**Yes.**

---

## 3.4 `<version>`

Example:

```xml
<version>0.0.1-SNAPSHOT</version>
```

This identifies the version of the application.

### Version breakdown

```text
0.0.1-SNAPSHOT
```

- `0` → major version
- `0` → minor version
- `1` → patch/revision
- `SNAPSHOT` → development version

### Common examples

Development version:

```xml
<version>0.1.0-SNAPSHOT</version>
```

First release:

```xml
<version>1.0.0</version>
```

Bug-fix release:

```xml
<version>1.0.1</version>
```

### Essential?

**Yes.**

---

## 3.5 `<relativePath/>`

```xml
<relativePath/>
```

This tells Maven not to search for the parent POM in the default relative location.

Normally Maven can check for a parent POM relative to the current project.

For Spring Boot projects using the Spring Boot parent, this is commonly written as:

```xml
<relativePath/>
```

---

# 4. Managing Maven Dependencies

The `pom.xml` should contain the dependencies required by the application.

### Good practices

- Only include dependencies that are required by the project.
- Remove unused dependencies.
- Avoid adding dependencies simply because they might be useful later.
- Keep the `pom.xml` easy to understand.
- Avoid unnecessary configuration.

### Why?

Unused dependencies can:

- Increase the size of the application
- Make the project harder to understand
- Introduce unnecessary transitive dependencies
- Increase the potential security attack surface
- Make dependency management more complicated

---

# 5. Application Architecture

The application uses a layered architecture.

```text
                    Client
                      |
                      v
              +---------------+
              |  Controller   |
              +---------------+
                      |
                      v
              +---------------+
              |    Service    |
              +---------------+
                      |
                      v
              +---------------+
              |  Repository   |
              +---------------+
                      |
                      v
              +---------------+
              |     JPA       |
              +---------------+
                      |
                      v
              +---------------+
              |   Hibernate   |
              +---------------+
                      |
                      v
              +---------------+
              | H2 Database   |
              +---------------+
```

Each layer has a specific responsibility.

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and responses |
| Service | Contains application/business logic |
| Repository | Handles data access |
| Entity | Represents database data |
| H2 | Stores the application data |

---

# 6. Entity Layer

An Entity represents a database table.

Example:

```java
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
}
```

### Important annotations

#### `@Entity`

Marks the class as a JPA entity.

```java
@Entity
```

Hibernate uses the entity to map Java objects to database records.

#### `@Id`

Identifies the primary key:

```java
@Id
```

#### `@GeneratedValue`

Allows the database/JPA to generate the ID:

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

---

# 7. Repository Layer

The repository is responsible for communicating with the database.

Example:

```java
public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
```

Spring Data JPA provides common database operations automatically.

Examples include:

```java
save()
findAll()
findById()
deleteById()
```

For example:

```java
studentRepository.findAll();
```

can result in a database query similar to:

```sql
SELECT * FROM students;
```

The repository therefore provides an abstraction over database operations.

---

# 8. Service Layer

The Service layer sits between the Controller and Repository.

The architecture is:

```text
Controller
    |
    v
Service
    |
    v
Repository
```

The purpose of the Service layer is to keep business/application logic out of the Controller.

Example:

```java
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
```

---

# 9. Service Interface

An interface in Java defines a contract.

It specifies **what operations are available**, without specifying how those operations are implemented.

Example:

```java
public interface StudentService {

    Student saveStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    void deleteStudent(Long id);
}
```

The implementation can then be:

```java
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
```

The relationship is:

```text
StudentService
      ^
      |
 implements
      |
      |
StudentServiceImpl
```

---

# 10. Why Use a Service Interface?

A service interface can be useful when there is a genuine need for abstraction.

## Recommended situations

### i. Multiple implementations

For example:

```text
              StudentService
                    |
          +---------+---------+
          |                   |
          v                   v
StudentServiceImpl    CachedStudentService
```

Different implementations can provide different behaviour while following the same contract.

---

### ii. Clean/Hexagonal Architecture

Interfaces can act as boundaries between the core application logic and external infrastructure such as:

- Databases
- External APIs
- Messaging systems
- Other services

---

### iii. External Libraries or SDKs

An interface can provide a stable contract while hiding implementation details.

---

### iv. Functional separation

Different interfaces can represent different responsibilities.

For example:

```java
public interface StudentReadService {
    List<Student> getAllStudents();
}
```

and:

```java
public interface StudentWriteService {
    Student saveStudent(Student student);
}
```

---

# 11. When a Service Interface May Not Be Necessary

An interface is not automatically required simply because a class is a Spring `@Service`.

A concrete service class is often sufficient when:

- There is only one implementation.
- The application is small.
- There is no meaningful abstraction.
- The interface would only duplicate the service methods.
- The additional files make the project harder to understand.

For example:

```text
StudentService.java
StudentServiceImpl.java
```

may be unnecessary if the application only ever needs:

```text
StudentService
```

with one implementation.

---

# 12. Controller Interfaces

Interfaces are generally less common for Controllers.

A typical Controller can simply be:

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
}
```

There is usually no need to create:

```text
StudentController.java
StudentControllerImpl.java
```

just for the sake of having an interface.

### Exception

Controller interfaces can be useful when using a design-first API approach, for example when an API contract is generated from an OpenAPI specification.

---

# 13. Complete Application Flow

## GET Request

When the client sends:

```http
GET http://localhost:8080/students
```

the request flows through the application:

```text
Client
  |
  | GET /students
  v
StudentController
  |
  v
StudentService
  |
  v
StudentServiceImpl
  |
  v
StudentRepository
  |
  v
Spring Data JPA
  |
  v
Hibernate
  |
  v
H2 Database
```

The data then travels back in the opposite direction:

```text
H2 Database
    |
    v
Hibernate
    |
    v
JPA Repository
    |
    v
Service
    |
    v
Controller
    |
    v
JSON Response
    |
    v
Client
```

---

# 14. POST Request

To save a student:

```http
POST http://localhost:8080/students
Content-Type: application/json
```

Request body:

```json
{
    "name": "Michael",
    "email": "michael@gmail.com"
}
```

The flow is:

```text
HTTP Request
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Hibernate
     |
     v
H2 Database
```

Hibernate converts the Java operation into SQL similar to:

```sql
INSERT INTO students (name, email)
VALUES ('Michael', 'michael@gmail.com');
```

---

# 15. Key Learning Points

The main concepts learned in this project are:

### Spring Boot

Spring Boot simplifies the configuration and development of Spring applications.

### Maven

Maven manages:

- Dependencies
- Project builds
- Plugins
- Packaging

### DataSource

The DataSource provides connections between the application and the database.

### H2

H2 is the database used for this learning project.

### JPA

JPA provides a standard way of mapping Java objects to relational database tables.

### Hibernate

Hibernate is the ORM implementation handling the mapping between Java objects and database records.

### Repository

The Repository handles database access.

### Service

The Service handles application/business logic.

### Controller

The Controller handles HTTP requests and exposes REST endpoints.

### Interface

An interface defines a contract that implementations can follow.

---

# 16. Current Architecture

The current application can therefore be represented as:

```text
                    ┌──────────────┐
                    │    Client    │
                    └──────┬───────┘
                           │
                           │ HTTP
                           ▼
                    ┌──────────────┐
                    │  Controller  │
                    │              │
                    │ Student      │
                    │ Controller   │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   Service    │
                    │  Interface   │
                    │              │
                    │ Student      │
                    │ Service      │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   Service    │
                    │ Implementation│
                    │              │
                    │ Student      │
                    │ ServiceImpl  │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  Repository  │
                    │              │
                    │ Student      │
                    │ Repository   │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │     JPA      │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  Hibernate   │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │ H2 Database  │
                    └──────────────┘
```

---

# 17. Project Structure

The project currently follows this structure:

```text
src
└── main
    ├── java
    │   └── h2demo
    │       └── example
    │           └── demo
    │               ├── Demo.java
    │               │
    │               ├── controller
    │               │   └── StudentController.java
    │               │
    │               ├── service
    │               │   ├── StudentService.java
    │               │   └── StudentServiceImpl.java
    │               │
    │               ├── repository
    │               │   └── StudentRepository.java
    │               │
    │               └── entity
    │                   └── Student.java
    │
    └── resources
        └── application.properties
```

---

# 18. Summary

The application has evolved from a simple Spring Boot application connected to H2 into a layered REST application.

The current architecture separates responsibilities:

```text
Controller
    ↓
Service Interface
    ↓
Service Implementation
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
H2 Database
```

This separation makes it easier to understand, maintain and extend the application as new functionality is introduced.


# REST (Representational State Transfer)

## 1. What is REST?

**REST** stands for **Representational State Transfer**.

REST is an **architectural style** used for designing applications and APIs that communicate over a network, usually using HTTP.

REST is **not**:

* A programming language
* A framework
* A database
* The same thing as JSON

In a Spring Boot application, we commonly use REST principles to create **REST APIs**.

---

## 2. REST API

A **REST API** allows a client to communicate with a server using HTTP requests.

For example, in our Student application:

```text
Client
   |
   | HTTP Request
   ↓
StudentController
   |
   ↓
StudentService
   |
   ↓
StudentRepository
   |
   ↓
H2 Database
```

The client could be:

* A web browser
* Postman
* IntelliJ HTTP Client
* A mobile application
* Another backend application
* JavaScript/React/Angular application

---

# 3. REST Resources

REST is built around **resources**.

A resource is something that the API manages.

For our application:

```text
Student = Resource
```

We represent the student resource using a URL:

```http
/students
```

A collection of students:

```http
/students
```

A specific student:

```http
/students/1
```

Where `1` identifies a particular student.

### Example

```text
/students
        ↓
All students

/students/1
        ↓
Student with ID 1

/students/2
        ↓
Student with ID 2
```

---

# 4. HTTP Methods in REST

REST APIs commonly use HTTP methods to indicate what operation should be performed on a resource.

| HTTP Method | Purpose               | Example              |
| ----------- | --------------------- | -------------------- |
| GET         | Retrieve data         | `GET /students`      |
| POST        | Create data           | `POST /students`     |
| PUT         | Replace/update data   | `PUT /students/1`    |
| PATCH       | Partially update data | `PATCH /students/1`  |
| DELETE      | Delete data           | `DELETE /students/1` |

---

## 5. GET

`GET` is used to **retrieve information**.

### Get all students

```http
GET /students
```

Example response:

```json
[
  {
    "id": 1,
    "name": "Michael",
    "age": 25
  },
  {
    "id": 2,
    "name": "John",
    "age": 22
  }
]
```

### Get one student

```http
GET /students/1
```

Example response:

```json
{
  "id": 1,
  "name": "Michael",
  "age": 25
}
```

---

# 6. POST

`POST` is normally used to **create a new resource**.

Example:

```http
POST /students
```

Request body:

```json
{
  "name": "Michael",
  "age": 25
}
```

The server processes the request and creates the student.

Possible response:

```json
{
  "id": 1,
  "name": "Michael",
  "age": 25
}
```

---

# 7. PUT

`PUT` is generally used to **replace/update an existing resource**.

Example:

```http
PUT /students/1
```

Request:

```json
{
  "name": "Michael Tigere",
  "age": 26
}
```

The server updates student `1`.

---

# 8. PATCH

`PATCH` is generally used for a **partial update**.

For example, if we only want to change the student's age:

```http
PATCH /students/1
```

Request:

```json
{
  "age": 26
}
```

Unlike `PUT`, we don't necessarily need to send the entire student representation.

---

# 9. DELETE

`DELETE` is used to remove a resource.

Example:

```http
DELETE /students/1
```

This tells the server:

```text
Delete student with ID 1
```

---

# 10. HTTP Request and Response

REST APIs communicate using a **request/response model**.

```text
Client
   |
   | HTTP Request
   ↓
Server
   |
   | HTTP Response
   ↓
Client
```

### Request

A request can contain:

```text
HTTP Method
URL
Headers
Body
```

Example:

```http
POST /students
Content-Type: application/json
```

Body:

```json
{
  "name": "Michael",
  "age": 25
}
```

### Response

The server sends back:

```text
HTTP Status Code
Headers
Body
```

Example:

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

Body:

```json
{
  "id": 1,
  "name": "Michael",
  "age": 25
}
```

---

# 11. JSON

REST APIs commonly use **JSON** to represent data.

JSON stands for:

**JavaScript Object Notation**

Example:

```json
{
  "id": 1,
  "name": "Michael",
  "age": 25
}
```

JSON is a **representation of data**.

REST itself does not require JSON. Other representations can be used, but JSON is very common in modern REST APIs.

---

# 12. RESTful URLs

REST APIs normally use URLs that represent **resources**, rather than actions.

### Prefer

```http
GET /students
GET /students/1
POST /students
DELETE /students/1
```

Instead of action-oriented URLs such as:

```http
GET /getStudents
POST /createStudent
GET /deleteStudent
```

The HTTP method already tells us what action is being performed.

For example:

```http
GET /students
```

means:

```text
GET → retrieve
/students → resource
```

---

# 13. REST is Stateless

One of the important characteristics of REST is **statelessness**.

Stateless means:

> Each client request must contain all the information necessary for the server to understand and process that request.

The server does **not rely on stored client-session state from previous requests**.

---

## 14. Stateless Does NOT Mean "No Data is Stored"

This is an important distinction.

A REST application can still store data in a database.

For example:

```text
H2 Database

Student 1 → Michael
Student 2 → John
Student 3 → Sarah
```

This is perfectly fine.

The database contains **application data**.

Statelessness refers to the server not needing to remember the client's previous interaction/session state in order to understand the current request.

---

# 15. How Can There Be Interaction if REST is Stateless?

Stateless does **not** mean that there is no interaction.

There is still a sequence of:

```text
Request → Response
Request → Response
Request → Response
```

For example:

```text
Client                         Server

GET /students  ───────────────→
              ←───────────────  Students

GET /students/1 ──────────────→
              ←───────────────  Student 1

POST /students ───────────────→
              ←───────────────  Created Student

DELETE /students/1 ───────────→
              ←───────────────  Deleted
```

The important point is that **each request can be understood independently**.

The server does not need to remember the previous request to understand the current one.

---

# 16. Stateful vs Stateless

## Stateful

In a stateful system, the server remembers information about the client's previous interactions.

For example:

```text
Client logs in
      ↓
Server creates session
      ↓
Server remembers:
"Michael is logged in"
      ↓
Client sends another request
      ↓
Server looks up the session
```

The current request depends on information stored from previous interactions.

---

## Stateless

In a stateless approach, the client sends the necessary information with each request.

For example:

```http
GET /students
Authorization: Bearer <token>
```

The server can process the request without needing to remember the client's previous request.

Conceptually:

```text
Request 1 → self-contained
Request 2 → self-contained
Request 3 → self-contained
```

---

# 17. Why is Statelessness Useful?

Statelessness makes REST applications easier to **scale**.

Imagine we have:

```text
             Load Balancer
                  |
        ┌─────────┼─────────┐
        ↓         ↓         ↓
    Server 1   Server 2   Server 3
```

A client's requests can go to different servers:

```text
Request 1 → Server 1
Request 2 → Server 3
Request 3 → Server 2
```

Because the servers do not need to maintain client-specific session state, any server can process the request.

This makes **horizontal scaling** easier.

---

# 18. REST and Spring Boot

In Spring Boot, we can create REST APIs using annotations such as:

```java
@RestController
@RequestMapping("/students")
```

Example:

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
```

---

# 19. Mapping HTTP Methods to Spring Boot

| REST Operation | HTTP Method | Spring Boot Annotation |
| -------------- | ----------- | ---------------------- |
| Retrieve all   | GET         | `@GetMapping`          |
| Retrieve one   | GET         | `@GetMapping("/{id}")` |
| Create         | POST        | `@PostMapping`         |
| Replace/update | PUT         | `@PutMapping`          |
| Partial update | PATCH       | `@PatchMapping`        |
| Delete         | DELETE      | `@DeleteMapping`       |

---

# 20. REST Request Flow in Our Application

Our Spring Boot Student application follows this flow:

```text
HTTP Client
     ↓
HTTP Request
     ↓
StudentController
     ↓
StudentService Interface
     ↓
StudentServiceImpl
     ↓
StudentRepository
     ↓
Spring Data JPA
     ↓
Hibernate
     ↓
H2 Database
     ↓
Response
     ↑
StudentController
     ↑
HTTP Client
```

For example:

```text
GET /students/1
        ↓
StudentController
        ↓
StudentService
        ↓
StudentServiceImpl
        ↓
StudentRepository
        ↓
Hibernate/JPA
        ↓
H2 Database
        ↓
Student 1
        ↓
JSON Response
```

---

# 21. Important REST Concepts to Remember

### REST

An architectural style for designing networked applications and APIs.

### Resource

Something managed by the API.

Example:

```text
Student
```

### URI/URL

Identifies a resource.

Example:

```text
/students/1
```

### HTTP Method

Describes what we want to do with the resource.

```text
GET     → Retrieve
POST    → Create
PUT     → Replace/Update
PATCH   → Partial Update
DELETE  → Delete
```

### Request

Information sent from the client to the server.

### Response

Information sent from the server back to the client.

### Statelessness

Each request contains the information needed for the server to process it without relying on stored client-session state from previous requests.

---

# 22. Simple REST Mental Model

A useful way to remember REST is:

```text
RESOURCE + HTTP METHOD + REPRESENTATION
```

For example:

```text
POST + /students + JSON
```

means:

```text
Create a student
```

And:

```text
GET + /students/1
```

means:

```text
Retrieve student 1
```

---

# 23. One-Sentence Summary

> **REST is an architectural style where clients interact with server-side resources through stateless HTTP requests, commonly using methods such as GET, POST, PUT, PATCH and DELETE, etc. with data often represented using JSON.**
