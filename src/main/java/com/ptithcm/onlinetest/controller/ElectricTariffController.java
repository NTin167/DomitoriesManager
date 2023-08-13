package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ElectricTariffEntity;
import com.ptithcm.onlinetest.service.ElectricTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ElectricTariffEntity> createElectricTariff(@RequestBody ElectricTariffEntity electricTariff) {
        ElectricTariffEntity createdElectricTariff = electricTariffService.createElectricTariff(electricTariff);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdElectricTariff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElectricTariffEntity> updateElectricTariff(@PathVariable Long id,
                                                                     @RequestBody ElectricTariffEntity updatedElectricTariff) {
        ElectricTariffEntity modifiedElectricTariff = electricTariffService.updateElectricTariff(id, updatedElectricTariff);
        if (modifiedElectricTariff != null) {
            return ResponseEntity.ok(modifiedElectricTariff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElectricTariff(@PathVariable Long id) {
        electricTariffService.deleteElectricTariff(id);
        return ResponseEntity.noContent().build();
    }
}
