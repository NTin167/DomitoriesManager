package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.StudentEntity;
import com.ptithcm.onlinetest.payload.dto.StudentDTO;
import com.ptithcm.onlinetest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDTO getStudentById(Long id) {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            StudentEntity student = studentOptional.get();
            return convertToDTO(student);
        }
        return null;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        StudentEntity student = convertToEntity(studentDTO);
        if (!studentRepository.existsByStudentCode(studentDTO.getStudentCode())) {
            StudentEntity createdStudent = studentRepository.save(student);
            return convertToDTO(createdStudent);
        } else {
            return null;
        }

    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        if (!studentRepository.existsByStudentCode(studentDTO.getStudentCode())) {
            if (studentOptional.isPresent()) {
                StudentEntity existingStudent = studentOptional.get();
                existingStudent.setStudentCode(studentDTO.getStudentCode());
                existingStudent.setGender(studentDTO.getGender());
                existingStudent.setDob(studentDTO.getDob());
                existingStudent.setIdentityCard(studentDTO.getIdentityCard());
                existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
                existingStudent.setEmail(studentDTO.getEmail());
//            existingStudent.setDeleted(studentDTO.isDeleted());
                StudentEntity updatedStudent = studentRepository.save(existingStudent);
                return convertToDTO(updatedStudent);
            }
        }
        return null;
    }


    private StudentDTO convertToDTO(StudentEntity student) {
        return new StudentDTO(
                student.getId(),
                student.getStudentCode(),
                student.getGender(),
                student.getDob(),
                student.getIdentityCard(),
                student.getPhoneNumber(),
                student.getEmail(),
                student.getDeleteYMD()
        );
    }

    private StudentEntity convertToEntity(StudentDTO studentDTO) {
        StudentEntity student = new StudentEntity();
        student.setStudentCode(studentDTO.getStudentCode());
        student.setGender(studentDTO.getGender());
        student.setDob(studentDTO.getDob());
        student.setIdentityCard(studentDTO.getIdentityCard());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setEmail(studentDTO.getEmail());
        return student;
    }
    public StudentDTO getStudentByStudentId(String studentId) {
        Optional<StudentEntity> studentOptional = studentRepository.findByStudentCode(studentId);
        if (studentOptional.isPresent()) {
            StudentEntity student = studentOptional.get();
            return convertToDTO(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        studentOptional.ifPresent(student -> {
            student.setDeleteYMD(LocalDate.now());
            studentRepository.save(student);
        });
    }
    public List<StudentDTO> getAllStudents() {
        List<StudentEntity> students = studentRepository.findByDeleteYMDIsNull();
        return students.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
