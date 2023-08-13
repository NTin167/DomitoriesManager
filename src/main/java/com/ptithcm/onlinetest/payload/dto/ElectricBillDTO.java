package com.ptithcm.onlinetest.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ElectricBillDTO {
    private Long id;
    private int electricNumber;
    private int price;
    private LocalDateTime createAt;
    private int status;
    private RoomDTO room;

}
