package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.ElectricBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ElectricBillRepository extends JpaRepository<ElectricBillEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ElectricBillEntity e WHERE YEAR(e.createAt) = :year AND MONTH(e.createAt) = :month")
    boolean existsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    boolean existsByCreateAtBetween(LocalDateTime from, LocalDateTime to);

    List<ElectricBillEntity> findAllByRoomId(Long roomId);
}
