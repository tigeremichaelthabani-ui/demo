package h2demo.example.demo.StudentController;
import h2demo.example.demo.StudentEntity.Students;
import h2demo.example.demo.StudentService.service;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {


    private final service studentService;


    public StudentController(service studentService) {
        this.studentService = studentService;
    }



    @GetMapping
    public List<Students> getStudents(){

        return studentService.getAllStudents();

    }



    @PostMapping
    public Students createStudent(@RequestBody Students student){

        return studentService.saveStudent(student);

    }


    @GetMapping("/{id}")
    public Students getStudent(@PathVariable Long id){

        return studentService.getStudentById(id);

    }


    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){

        studentService.deleteStudent(id);

    }

}