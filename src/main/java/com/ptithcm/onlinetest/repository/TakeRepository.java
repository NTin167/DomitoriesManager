package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.model.Take;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {
    Iterable<Take> findAllByUserId(Long id);
}
