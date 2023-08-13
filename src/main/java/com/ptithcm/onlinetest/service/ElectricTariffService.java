package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import com.ptithcm.onlinetest.repository.ElectricTariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectricTariffService {
    @Autowired
    private ElectricTariffRepository electricTariffRepository;

    public List<ElectricTariffEntity> getAllElectricTariffs() {
        return electricTariffRepository.findAll();
    }

    public ElectricTariffEntity getElectricTariffById(Long id) {
        return electricTariffRepository.findById(id).orElse(null);
    }

    public ElectricTariffEntity createElectricTariff(ElectricTariffEntity electricTariff) {
        return electricTariffRepository.save(electricTariff);
    }

    public ElectricTariffEntity updateElectricTariff(Long id, ElectricTariffEntity updatedElectricTariff) {
        ElectricTariffEntity existingElectricTariff = electricTariffRepository.findById(id).orElse(null);
        if (existingElectricTariff != null) {
            existingElectricTariff.setMonth(updatedElectricTariff.getMonth());
            existingElectricTariff.setYear(updatedElectricTariff.getYear());
            existingElectricTariff.setPrice(updatedElectricTariff.getPrice());
            return electricTariffRepository.save(existingElectricTariff);
        }
        return null;
    }

    public void deleteElectricTariff(Long id) {
        electricTariffRepository.deleteById(id);
    }
}
