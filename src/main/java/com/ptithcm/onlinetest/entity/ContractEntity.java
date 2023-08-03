package com.ptithcm.onlinetest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "contracts")
@Entity
@NoArgsConstructor
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractId;
    private LocalDate createAt;
    private int price;
    private int status;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private int leaseDuration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;
    // Quan hệ nhiều hợp đồng thuộc về 1 sinh viên
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity student;
    // Quan hệ nhiều hợp đồng thuộc về 1 sinh viên
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    // Quan hệ 1 contract có nhiều Invoice
    @OneToMany(mappedBy = "contract")
    private List<InvoiceEntity> invoices = new ArrayList<>();
}
