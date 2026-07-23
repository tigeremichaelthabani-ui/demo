package h2demo.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;

@SpringBootApplication
public class DemoApplication {


	public class Main {
		public static void main(String... args) {
			String url = "jdbc:mysql://localhost:3306/your_database";
			String user = "your_username";
			String password = "your_password";

			try (var conn = java.sql.DriverManager.getConnection(url, user, password)) {
				System.out.println("Connection established successfully!");
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
			{
				System.out.println("Connected to the MySQL database.");
			}
		}
	}
}