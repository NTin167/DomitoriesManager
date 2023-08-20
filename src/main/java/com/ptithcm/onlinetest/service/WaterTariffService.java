package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.WaterTariffEntity;
import com.ptithcm.onlinetest.repository.WaterTariffRepository;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaterTariffService {
    @Autowired
    private WaterTariffRepository waterTariffRepository;

    public List<WaterTariffEntity> getAllWaterTariffs() {
        return waterTariffRepository.findAll();
    }

    public WaterTariffEntity getWaterTariffById(Long id) {
        return waterTariffRepository.findById(id).orElse(null);
    }

    public int createWaterTariff(WaterTariffEntity waterTariff) {
        List<WaterTariffEntity> waterTariffEntities = waterTariffRepository.findAll();
        for (WaterTariffEntity w : waterTariffEntities) {
            if (w.getYear() == waterTariff.getYear() && w.getMonth() == waterTariff.getMonth()) {
                return 1;
            }
        }
        waterTariffRepository.save(waterTariff);
        return 0;
    }

    public GenericResponse updateWaterTariff(Long id, WaterTariffEntity updatedWaterTariff) {
        Optional<WaterTariffEntity> existingWaterTariff = waterTariffRepository.findById(id);
        if (existingWaterTariff.isPresent()) {
            if (existingWaterTariff.get().getWaterBills().isEmpty()) {
                existingWaterTariff.get().setMonth(updatedWaterTariff.getMonth());
                existingWaterTariff.get().setYear(updatedWaterTariff.getYear());
                existingWaterTariff.get().setPrice(updatedWaterTariff.getPrice());
                waterTariffRepository.save(existingWaterTariff.get());
                return new GenericResponse("Sửa bảng giá nước thành công");
            } else {
                return new GenericResponse("Phiếu nước đã có bảng giá. Không thể sửa");
            }
        } else {
            return new GenericResponse("Bảng giá nước không tồn tại. Sửa bảng giá nước thất bại");
        }
    }

    public GenericResponse deleteWaterTariff(Long id) {
        Optional<WaterTariffEntity> existingWaterTariff = waterTariffRepository.findById(id);
        if (existingWaterTariff.isPresent()) {
            if (!existingWaterTariff.get().getWaterBills().isEmpty()) {
                return new GenericResponse("Phiếu nước đã có bảng giá. Không thể xóa");
            } else {
                waterTariffRepository.deleteById(id);
                return new GenericResponse("Xóa bảng giá nước thành công");
            }
        } else {
            return new GenericResponse("Bảng giá nước không tồn tại");
        }
    }
}
