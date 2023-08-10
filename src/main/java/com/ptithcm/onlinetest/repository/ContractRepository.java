package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    // Add custom queries if needed
    List<ContractEntity> findAllByStudentId(Long studentId);
}