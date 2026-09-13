package h2demo.example.demo.StudentController;
import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentService.StudentServiceImpl;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentServiceImpl studentStudentServiceImp;


    public StudentController(StudentServiceImpl studentStudentServiceImp) {
        this.studentStudentServiceImp = studentStudentServiceImp;
    }



    @GetMapping
    public List<Students> getStudents(){

        return studentStudentServiceImp.getAllStudents();

    }



    @PostMapping
    public Students createStudent(@RequestBody Students student){

        return studentStudentServiceImp.saveStudent(student);

    }


    @GetMapping("/{id}")
    public Students getStudent(@PathVariable Long id){

        return studentStudentServiceImp.getStudentById(id);

    }
    @PutMapping("/{id}")
    public Students updateStudent(
            @PathVariable Long id,
            @RequestBody Students student) {

        Students students;
        students = studentStudentServiceImp.updateStudent(id, student);
        return students;
    }


    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){

        studentStudentServiceImp.deleteStudent(id);

    }

}