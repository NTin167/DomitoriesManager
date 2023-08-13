package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricTariffRepository extends JpaRepository<ElectricTariffEntity, Long> {
}
