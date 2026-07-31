package h2demo.example.demo.StudentController;
import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentRepository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }


    // Insert data
    @PostMapping
    public Students addStudent(@RequestBody Students student) {
        return repository.save(student);
    }


    // Retrieve all data
    @GetMapping
    public List<Students> getStudents() {
        return repository.findAll();
    }


    // Retrieve one student
    @GetMapping("/{id}")
    public Students getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElse(null);
    }
}