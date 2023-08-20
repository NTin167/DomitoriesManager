package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import com.ptithcm.onlinetest.repository.ElectricTariffRepository;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public int createElectricTariff(ElectricTariffEntity electricTariff) {
        List <ElectricTariffEntity> electricTariffEntities = electricTariffRepository.findAll();
        for (ElectricTariffEntity e : electricTariffEntities) {
            if (e.getYear() == electricTariff.getYear() && e.getMonth() == electricTariff.getMonth()) {
                return 1;
            }
        }
        electricTariffRepository.save(electricTariff);
        return 0;
    }

    public GenericResponse updateElectricTariff(Long id, ElectricTariffEntity updatedElectricTariff) {
        Optional<ElectricTariffEntity> existingElectricTariff = electricTariffRepository.findById(id);
        if (existingElectricTariff.isPresent()) {
            if(existingElectricTariff.get().getElectricBills().isEmpty()) {
                existingElectricTariff.get().setMonth(updatedElectricTariff.getMonth());
                existingElectricTariff.get().setYear(updatedElectricTariff.getYear());
                existingElectricTariff.get().setPrice(updatedElectricTariff.getPrice());
                electricTariffRepository.save(existingElectricTariff.get());
                return new GenericResponse("Sửa bảng giá điện thành công");
            } else {
                return new GenericResponse("Phiếu điện đã có bảng giá. Không thể sửa");
            }

        } else {
            return new GenericResponse("Bảng giá điện không tồn tại. Sửa bảng giá điện thất bại");
        }
    }

    public GenericResponse deleteElectricTariff(Long id) {
        Optional<ElectricTariffEntity> existingElectricTariff = electricTariffRepository.findById(id);
        if (existingElectricTariff.isPresent()) {
            if(!existingElectricTariff.get().getElectricBills().isEmpty()) {
                return new GenericResponse("Phiếu điện đã có bảng giá. Không thể xóa");
            }
            else {
                electricTariffRepository.deleteById(id);
                return new GenericResponse("Xóa bảng giá điện thành công");
            }
        } else {
            return new GenericResponse("Bảng giá điện không tồn tại");
        }
    }
}
