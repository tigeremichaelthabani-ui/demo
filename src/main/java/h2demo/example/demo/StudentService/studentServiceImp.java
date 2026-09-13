package h2demo.example.demo.StudentService;
import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentRepository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class studentServiceImp {


    private StudentRepository studentRepository;

    public studentServiceImp(StudentRepository studentRepository) {
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

    public Students updateStudent(Long id, Students student) {

        Students existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());

        return studentRepository.save(existingStudent);
    }

    private Long id;
    Students students = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
