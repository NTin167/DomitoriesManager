package com.ptithcm.onlinetest.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Long id;
    private String staffCode;
    private String name;
    private int gender;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
    private LocalDate deleteYMD;
}