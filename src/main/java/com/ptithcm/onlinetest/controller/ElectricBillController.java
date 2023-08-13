package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ElectricBillEntity;
import com.ptithcm.onlinetest.payload.dto.ElectricBillDTO;
import com.ptithcm.onlinetest.repository.ElectricBillRepository;
import com.ptithcm.onlinetest.service.ElectricBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/electric-bills")
public class ElectricBillController {

    private final ElectricBillService electricBillService;

    @Autowired
    private ElectricBillRepository electricBillRepository;

    @Autowired
    public ElectricBillController(ElectricBillService electricBillService) {
        this.electricBillService = electricBillService;
    }

    // Create a new electric bill
    @PostMapping
    public ResponseEntity<?> createElectricBill(@RequestBody ElectricBillDTO electricBillDTO) {
        try {
            List<ElectricBillEntity> electricBillEntities = electricBillRepository.findAllByRoomId(electricBillDTO.getRoom().getId());

            if (electricBillEntities.isEmpty()) { // chưa có hóa đơn nào
                ElectricBillDTO createdElectricBill = electricBillService.createElectricBill(electricBillDTO);
                return ResponseEntity.ok(createdElectricBill);
            } else  {
                for (ElectricBillEntity electricBillEntity : electricBillEntities) {
                    if(electricBillEntity != null) {
                        int year = electricBillEntity.getCreateAt().getYear();
                        int month = electricBillEntity.getCreateAt().getMonthValue();

                        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
                        LocalDateTime to = from.plusMonths(1).minusNanos(1);
                        boolean exists = electricBillService.existsElectricBillForYearAndMonth(from, to);
                        if (exists) {
                            return new ResponseEntity<>("Đã có hóa đơn tháng năm này", HttpStatus.BAD_REQUEST);
                        }
                    }
//                return new ResponseEntity<>("Hóa đơn không tồn tại", HttpStatus.BAD_REQUEST);


                }
            }

//        int year = electricBillDTO.getCreateAt().getYear();
//        int month = electricBillDTO.getCreateAt().getMonthValue();
//
//        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
//        LocalDateTime to = from.plusMonths(1).minusNanos(1);

//        boolean exists = electricBillService.existsElectricBillForYearAndMonth(from, to);
//        if (exists) {
//            return new ResponseEntity<>("NOT FOUND", HttpStatus.BAD_REQUEST);
//        }

            ElectricBillDTO createdElectricBill = electricBillService.createElectricBill(electricBillDTO);
            return ResponseEntity.ok(createdElectricBill);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // Read all electric bills
    @GetMapping
    public ResponseEntity<List<ElectricBillDTO>> getAllElectricBills() {
        List<ElectricBillDTO> electricBills = electricBillService.getAllElectricBills();
        return ResponseEntity.ok(electricBills);
    }

    // Read an electric bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<ElectricBillDTO> getElectricBillById(@PathVariable Long id) {
        ElectricBillDTO electricBill = electricBillService.getElectricBillById(id);
        return ResponseEntity.ok(electricBill);
    }

    // Update an electric bill
    @PutMapping("/{id}")
    public ResponseEntity<ElectricBillDTO> updateElectricBill(
            @PathVariable Long id, @RequestBody ElectricBillDTO electricBillDTO) {
        ElectricBillDTO updatedElectricBill = electricBillService.updateElectricBill(id, electricBillDTO);
        return ResponseEntity.ok(updatedElectricBill);
    }

    // Delete an electric bill
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElectricBill(@PathVariable Long id) {
        electricBillService.deleteElectricBill(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/payment/{id}")
    public ResponseEntity<?> payment(
            @PathVariable Long id) {
        int paymentElectricBill = electricBillService.paymentElectricBill(id);
        if(paymentElectricBill == 0 ) {
            return new ResponseEntity<>("Thanh toán thất bại", HttpStatus.BAD_REQUEST);
        } else if (paymentElectricBill == 2) {
            return new ResponseEntity<>("Hóa đơn đã thanh toán", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Thanh toán hóa đơn thành công", HttpStatus.OK);
    }
}