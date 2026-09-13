package h2demo.example.demo.StudentService;

import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentRepository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * @param student
     * @return
     */
    @Override
    public Students saveStudent(Students student) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Students> getAllStudents() {
        return List.of();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Students getStudentById(Long id) {
        return null;
    }

    @Override
    public Students updateStudent(Long id, Students student) {

        Students existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());

        return studentRepository.save(existingStudent);
    }

    /**
     * @param id
     */
    @Override
    public void deleteStudent(Long id) {

    }
}
