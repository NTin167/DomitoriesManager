package com.ptithcm.onlinetest.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class WaterBillDTO {
    private Long id;
    private int waterNumber;
    private int price;
    private LocalDateTime createAt;
    private int status;
    private RoomDTO room;

}
