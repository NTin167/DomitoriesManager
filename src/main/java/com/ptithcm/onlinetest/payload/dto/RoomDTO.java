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
    private String roomTypeName;
    private int price;
    private String description;

    private String linkImg;

    public RoomDTO(Long id, String roomName, int status, int totalCapacity, int availableCapacity, Long roomTypeId, String roomTypeName, int price, String description, String linkImg) {
        this.id = id;
        this.roomName = roomName;
        this.status = status;
        this.totalCapacity = totalCapacity;
        this.availableCapacity = availableCapacity;
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.price = price;
        this.description = description;
        this.linkImg = linkImg;
    }

    public RoomDTO() {
    }
}
