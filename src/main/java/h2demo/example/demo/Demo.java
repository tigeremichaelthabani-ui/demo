package h2demo.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class Demo {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Demo.class, args);
        DataSource dataSource = context.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection()) {

            System.out.println("Connected to: "
                    + connection.getMetaData()
                    .getDatabaseProductName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
