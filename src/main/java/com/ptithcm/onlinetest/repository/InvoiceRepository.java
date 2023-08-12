package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    List<InvoiceEntity> findAllByContract_Student_Id(Long studentId);
}
