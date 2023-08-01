package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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

}

