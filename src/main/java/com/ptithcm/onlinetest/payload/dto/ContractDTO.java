package com.ptithcm.onlinetest.payload.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractDTO {
    private Long id;
    private String contractId;
    private LocalDate createAt;
    private int price;
    private int status;
    private LocalDate dateStart;
    private int leaseDuration;
    private LocalDate dateEnd;
    private Long staffId;
    private String staffName;
    private Long studentId;
    private String studentName;
    private Long roomId;
    private String roomName;
    private List<Long> invoiceIds;
    private int expiryStatus;
}