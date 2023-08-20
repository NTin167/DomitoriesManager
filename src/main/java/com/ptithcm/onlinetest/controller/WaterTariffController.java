package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.WaterTariffEntity;
import com.ptithcm.onlinetest.service.WaterTariffService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water-tariffs")
public class WaterTariffController {

    @Autowired
    private WaterTariffService waterTariffService;

    @GetMapping
    public List<WaterTariffEntity> getAllWaterTariffs() {
        return waterTariffService.getAllWaterTariffs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterTariffEntity> getWaterTariffById(@PathVariable Long id) {
        WaterTariffEntity waterTariff = waterTariffService.getWaterTariffById(id);
        if (waterTariff != null) {
            return ResponseEntity.ok(waterTariff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public GenericResponse createWaterTariff(@RequestBody WaterTariffEntity waterTariff) {
        int result = waterTariffService.createWaterTariff(waterTariff);
        if (result == 0) {
            return new GenericResponse("Thêm mới bảng giá nước thành công");
        } else if (result == 1) {
            return new GenericResponse("Ngày tháng này đã có bảng giá nước");
        }
        return new GenericResponse("Lỗi không xác định");
    }

    @PutMapping("/{id}")
    public GenericResponse updateWaterTariff(@PathVariable Long id,
                                             @RequestBody WaterTariffEntity updatedWaterTariff) {
        return waterTariffService.updateWaterTariff(id, updatedWaterTariff);
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteWaterTariff(@PathVariable Long id) {
        return waterTariffService.deleteWaterTariff(id);
    }
}
