package com.ptithcm.onlinetest.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
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
