package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<StaffEntity, Long> {
    List<StaffEntity> findByDeleteYMDIsNull();
    boolean existsByStaffCode(String studentCode);
}
