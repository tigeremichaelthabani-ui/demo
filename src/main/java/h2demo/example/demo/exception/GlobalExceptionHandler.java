package h2demo.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFound(
            StudentNotFoundException exception) {

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", 404);
        errorResponse.put("error", "Student Not Found");
        errorResponse.put("message", exception.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception exception) {

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", 500);
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", exception.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private class StudentNotFoundException extends Throwable {
    }
}


