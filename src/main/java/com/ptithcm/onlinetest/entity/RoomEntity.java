package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String linkImg;
    // Quan hệ 1 room thuộc nhiều hợp đồng
    @OneToMany(mappedBy = "room")
    private List<ContractEntity> contracts = new ArrayList<>();

    // Quan hệ mỗi room thuộc về 1 room_types
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomTypeId")
    private RoomTypesEntity roomType;
}