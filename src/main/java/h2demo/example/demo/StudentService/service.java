package h2demo.example.demo.StudentService;
import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentRepository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class service {


    private final StudentRepository studentRepository;


    public service(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    // Save student
    public Students saveStudent(Students student) {

        return studentRepository.save(student);

    }


    // Retrieve all students
    public List<Students> getAllStudents() {

        return studentRepository.findAll();

    }


    // Retrieve one student
    public Students getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElse(null);

    }


    // Delete student
    public void deleteStudent(Long id) {

        studentRepository.deleteById(id);

    }
}