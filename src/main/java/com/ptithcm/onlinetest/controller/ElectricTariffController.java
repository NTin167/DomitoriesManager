package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import com.ptithcm.onlinetest.service.ElectricTariffService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/electric-tariffs")
public class ElectricTariffController {

    @Autowired
    private ElectricTariffService electricTariffService;

    @GetMapping
    public List<ElectricTariffEntity> getAllElectricTariffs() {
        return electricTariffService.getAllElectricTariffs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectricTariffEntity> getElectricTariffById(@PathVariable Long id) {
        ElectricTariffEntity electricTariff = electricTariffService.getElectricTariffById(id);
        if (electricTariff != null) {
            return ResponseEntity.ok(electricTariff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public GenericResponse createElectricTariff(@RequestBody ElectricTariffEntity electricTariff) {
        int result = electricTariffService.createElectricTariff(electricTariff);
        if (result == 0) {
            return new GenericResponse("Thêm mới bảng giá điện thành công");
        } else if(result == 1 ) {
            return new GenericResponse("Ngày tháng này đã có bảng giá điện");
        }
        return new GenericResponse("Lỗi không xác định");
    }

    @PutMapping("/{id}")
    public GenericResponse updateElectricTariff(@PathVariable Long id,
                                                                     @RequestBody ElectricTariffEntity updatedElectricTariff) {
        return electricTariffService.updateElectricTariff(id, updatedElectricTariff);
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteElectricTariff(@PathVariable Long id) {
        return electricTariffService.deleteElectricTariff(id);
    }
}
