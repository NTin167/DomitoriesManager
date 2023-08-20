package com.ptithcm.onlinetest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "water_tariff")
@Entity
@NoArgsConstructor
public class WaterTariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int month;
    private int year;
    private int price;
    // Quan hệ 1 ElectricTariffEntity có nhiều ElectricBillEntity
    @OneToMany(mappedBy = "waterTariff")
    @JsonIgnore
    private List<WaterBillEntity> waterBills = new ArrayList<>();
}
