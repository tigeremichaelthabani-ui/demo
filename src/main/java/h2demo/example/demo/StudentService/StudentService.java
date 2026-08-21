package h2demo.example.demo.StudentService;

import h2demo.example.demo.StudentEntity.Students;

import java.util.List;

public interface StudentService {

    Students saveStudent(Students student);

    List<Students> getAllStudents();

    Students getStudentById(Long id);

    void deleteStudent(Long id);
}