1.Configuring a datasource - database
- Adding database as dependency to the Spring Boot application - H2 database
    - Added to the pom.xml file, the dependency required to add the database
      <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
      </dependency>
      What is the purpose of scope in a Spring Boot project when you add the dependency like com.h2database in a mvn project pom.xml file?
      compile - when compling and running
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