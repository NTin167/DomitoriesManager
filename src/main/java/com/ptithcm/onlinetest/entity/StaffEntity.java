package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Table(name = "staffs")
@Entity
@NoArgsConstructor
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String staffCode;
    private String name;
    private int gender;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
    private LocalDate deleteYMD;
}

