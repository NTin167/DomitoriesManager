package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
}

