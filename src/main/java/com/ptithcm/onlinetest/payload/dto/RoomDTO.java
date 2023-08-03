package com.ptithcm.onlinetest.payload.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String roomName;
    private int status;
    private int totalCapacity;
    private int availableCapacity;
    private Long roomTypeId;

    private String linkImg;
}
