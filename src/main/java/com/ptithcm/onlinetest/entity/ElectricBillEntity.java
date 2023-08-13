package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "electric_bill")
@Entity
@NoArgsConstructor
public class ElectricBillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int electricNumber;
    private int price;
    private LocalDateTime createAt;
    private int status;
    // Quan hệ nhiều electric bills thuộc về 1 room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    // Quan hệ nhiều ElectricBillEntity thuộc về 1 ElectricTariffEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electric_tariff_id")
    private ElectricTariffEntity electricTariff;
}
