package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "rooms")
@Entity
@NoArgsConstructor
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private int status;
    private int totalCapacity;
    private int availableCapacity;
    private Long roomTypeId;
}