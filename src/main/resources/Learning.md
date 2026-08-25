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