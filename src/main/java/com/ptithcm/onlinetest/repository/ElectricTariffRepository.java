package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricTariffRepository extends JpaRepository<ElectricTariffEntity, Long> {
    @Query("SELECT et FROM ElectricTariffEntity et WHERE et.year = (SELECT MAX(et.year) FROM ElectricTariffEntity et) AND et.month = (SELECT MAX(et.month) FROM ElectricTariffEntity et WHERE et.year = (SELECT MAX(et.year) FROM ElectricTariffEntity et))")
    ElectricTariffEntity findLatestElectricTariff();
}
