package h2demo.example.demo.StudentService;

import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentRepository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface StudentService {

    Students saveStudent(Students student);

    List<Students> getAllStudents();

    Students getStudentById(Long id);

    Students updateStudent(Long id, Students student);

    void deleteStudent(Long id);


}
