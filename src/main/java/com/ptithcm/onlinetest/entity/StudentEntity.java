package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "students")
@Entity
@NoArgsConstructor
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String studentCode;
    private int gender;
    private LocalDate dob;
    private String identityCard;
    private String phoneNumber;
    private String email;
    private LocalDate deleteYMD;
    // Quan hệ 1 sinh viên có nhiều hợp đồng
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ContractEntity> contracts = new ArrayList<>();
}

