package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.payload.dto.StudentDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.StudentRepository;
import com.ptithcm.onlinetest.service.StudentService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    ContractRepository contractRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    StudentRepository studentRepository;
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public GenericResponse createStudent(@RequestBody StudentDTO studentDTO) {
        int result = studentService.createStudent(studentDTO);
        if (result == 0) {
            return new GenericResponse("Thêm mới sinh viên thành công");
        } else if (result == 1) {
            return new GenericResponse("Mã sinh viên đã tồn tại");
        }
        return new GenericResponse("Xử lý thất bại");
    }

    @PutMapping("/{id}")
    public GenericResponse updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        int result = studentService.updateStudent(id, studentDTO);
        if (result == 0) {
            return new GenericResponse("Sửa sinh viên thành công");
        }
        return new GenericResponse("Sinh viên không tồn tại");
    }

    @GetMapping("/getByStudentCode/{studentCode}")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@PathVariable String studentCode) {
        StudentDTO student = studentService.getStudentByStudentId(studentCode);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteStudent(@PathVariable Long id) {
        int result = studentService.deleteStudent(id);
        if(result == 0) {
            return new GenericResponse("Xóa thành công");
        } else if (result == 1) {
            return new GenericResponse("Sinh viên không tồn tại");
        } else if (result == 2) {
            return new GenericResponse("Sinh viên đã đăng ký phòng. Không thể xóa!");
        }
        return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý!");
    }
}
