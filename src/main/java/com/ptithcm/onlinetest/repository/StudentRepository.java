package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByStudentCode(String studentCode);
    List<StudentEntity> findByDeleteYMDIsNull();

    boolean existsByStudentCode(String studentCode);
}

