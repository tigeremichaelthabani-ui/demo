package h2demo.example.demo.StudentEntity;
import jakarta.persistence.*;


    @SuppressWarnings("ALL")
    @Entity
    @Table(name = "students")
    public class Students {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String email;

        public Students() {
        }

        public Students(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
