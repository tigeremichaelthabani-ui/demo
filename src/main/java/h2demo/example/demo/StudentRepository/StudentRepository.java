package h2demo.example.demo.StudentRepository;

import h2demo.example.demo.StudentEntity.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Students, Long> {

}