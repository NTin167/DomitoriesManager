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
    private Long studentId;
    private Long roomId;
    private List<Long> invoiceIds;
}