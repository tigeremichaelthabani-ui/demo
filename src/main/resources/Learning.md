1.Configuring a datasource - database
- Adding database as dependency to the Spring Boot application - H2 database
    - Added to the pom.xml file, the dependency required to add the database
      <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
      </dependency>
      What is the purpose of scope in a Spring Boot project when you add the dependency like com.h2database in a mvn project pom.xml file?
      compile - when compiling and running
      test
      runtime - when running
      not specified - default compile

**_2.Adding the database configurations_**
**Two ways**
**Auto-configuration** - you use an application properties file
spring.application.name=demo
# Database Connection
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console Settings
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Settings (Optional, to auto-create tables from entities)
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update


**Manual** - adding a configuration class using @Configuration annotation

@Configuration
public class DatabaseConfig {

  '''  @Value("${spring.datasource.url}")
    private String dbUrl;

  '''  @Value("${spring.datasource.username}")
    private String dbUsername;

   ''' @Value("${spring.datasource.password}")
    private String dbPassword;

  '''  @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

   ''' @Bean
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






ESSENTIAL TAGS AND ANNOTATIONS IN THE POM.XML FILE:

1. <parent>
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.0</version>
    <relativePath/>
</parent>

The <parent> tells Maven:

"This project inherits configuration and dependency management rules from Spring Boot."

Think of it as a template that provides sensible defaults.

It gives you:

✅ Compatible dependency versions
✅ Maven plugin configuration
✅ Java compiler settings
✅ Spring Boot packaging support
✅ Default configuration for building and running your application

Without the Spring Boot parent, you would have to manually specify versions for many dependencies.

For example, instead of:

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

you would need something like:

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>...</version>
</dependency>

The parent manages those versions for you.

Essential? ✅ Recommended for most Spring Boot projects.

2. <groupId>
<groupId>h2demo.example</groupId>

This identifies the organization or namespace that owns the project.

Think of it like a Java package name.

Examples:

<groupId>com.google</groupId>
<groupId>org.springframework</groupId>
<groupId>com.mycompany</groupId>

Your project:

<groupId>h2demo.example</groupId>

matches your Java package:

h2demo.example.demo

It helps Maven uniquely identify your application.

Essential? ✅ Yes.

3. <artifactId>
<artifactId>demo</artifactId>

This is the name of the actual application artifact.

It is used when Maven builds your project.

Example:

mvn clean package

creates:

demo-0.0.1-SNAPSHOT.jar

because:

artifactId = demo
version = 0.0.1-SNAPSHOT

You could change it:

<artifactId>student-management-api</artifactId>

and Maven would create:

student-management-api-0.0.1-SNAPSHOT.jar

Essential? ✅ Yes.

4. <version>
<version>0.0.1-SNAPSHOT</version>

This is your application version.
</version>
Meaning:

0.0.1 

= first development version

SNAPSHOT

= still under development

Common versions:

Development:

<version>0.0.1-SNAPSHOT</version>

First release:

<version>1.0.0</version>

Bug fix:

<version>1.0.1</version>

Essential? ✅ Yes.

5. <relativePath/>
<relativePath/>

This tells Maven:

"Do not look for the parent pom.xml locally; download it from Maven repositories."

Normally Maven checks:

Your project
|
└── ../pom.xml

before going online.

The empty tag disables that.

For Spring Boot projects, this is standard:

INTERFACES
•	interface in Java is a blueprint or contract that defines a set of methods without implementing their logic. It tells a class what it must do, but not how to do it.
In a Spring Boot application, deciding whether to use interfaces for your Controllers and Services depends heavily on your specific architecture, scaling needs, and team preferences.
1. Controllers: Interfaces are Rarely Recommended
   It is not recommended to create interfaces for your Controllers (e.g., UserController and UserControllerImpl)
   •	Why you should avoid them: Controllers form the entry point of your HTTP web layer. They are bound directly to web-specific mapping annotations like @GetMapping or @PostMapping. Since a Controller class is almost never swapped out for a different implementation or reused by other classes, an interface adds redundant, boilerplate code with zero architectural benefit.
   •	The Exception: The only time a Controller interface is highly useful is if you are using a design-first API approach (like OpenAPI/Swagger Code Generator). The tool automatically generates Java interfaces containing the HTTP mapping metadata, and you simply write a class that implements them.
2. Services: When to Use vs. Avoid Interfaces
   Historically, early versions of Spring required interfaces to perform tasks like transaction management via JDK dynamic proxies. Modern Spring Boot uses CGLIB subclass-based proxying by default, meaning interfaces are completely optional.
   Instead of a blanket rule, evaluate your specific project against these criteria:
   RECOMMENDED: Use an Interface for a Service When:
   •	You have multiple implementations: If you have an OrderService interface, but need a CreditCardOrderService and a CryptoOrderService to handle different payment routes, an interface allows you to cleanly swap implementations using Spring's @Qualifier annotation.
   •	You are building an external Library/SDK: If you are publishing your service logic for other teams or external apps to use, exposing only the interface encapsulates your implementation details cleanly.
   •	You strictly follow Clean/Hexagonal Architecture: If you are isolating your pure business domain from external infrastructure (like databases or third-party APIs), interfaces act as ports that keep your core logic loosely coupled.
   •	You need functional segregation: You want to restrict what certain parts of your app can see. For example, separating read actions from write actions via a ReadOnlyUserService interface.
   🔴 NOT RECOMMENDED: Avoid Interfaces (Use Concrete Classes) When:
   •	It is a 1-to-1 relationship: If every single MyService interface in your app has exactly one MyServiceImpl class, you are adding double the files and maintenance for no functional gain. 
   •	Testing is your only reason: Developers used to create interfaces just to mock services in unit tests. Today, modern testing tools like Mockito let you mock concrete classes (@Mock UserService) seamlessly without requiring an interface.
   •	You are building a standard CRUD/Microservice application: For standard microservices with linear database operations, concrete service classes keep the project lean, readable, and faster to refactor. 

