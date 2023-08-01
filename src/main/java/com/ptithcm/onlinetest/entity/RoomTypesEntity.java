package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "room_types")
@Entity
@NoArgsConstructor
public class RoomTypesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int bedNumber;
    private String image;
    private int price;
    private String name;
    private String roomGender;
    private String description;
}