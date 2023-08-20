package com.ptithcm.onlinetest.repository;

import com.ptithcm.onlinetest.entity.WaterTariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterTariffRepository extends JpaRepository<WaterTariffEntity, Long> {
    @Query("SELECT wt FROM WaterTariffEntity wt WHERE wt.year = (SELECT MAX(wt.year) FROM WaterTariffEntity wt) AND wt.month = (SELECT MAX(wt.month) FROM WaterTariffEntity wt WHERE wt.year = (SELECT MAX(wt.year) FROM WaterTariffEntity wt))")
    WaterTariffEntity findLatestWaterTariff();
}
