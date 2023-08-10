package com.ptithcm.onlinetest.payload.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private Long id;
    private String invoiceId;
    private LocalDate createAt;
    private int price;
    private int status;
    private ContractDTO contract;

    // Constructors, getters, setters
}
