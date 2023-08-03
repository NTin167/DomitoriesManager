package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContractEntity> contracts;
}

