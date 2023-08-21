package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.WaterBillEntity;
import com.ptithcm.onlinetest.payload.dto.WaterBillDTO;
import com.ptithcm.onlinetest.repository.WaterBillRepository;
import com.ptithcm.onlinetest.service.WaterBillService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/water-bills")
public class WaterBillController {

    private final WaterBillService waterBillService;

    @Autowired
    private WaterBillRepository waterBillRepository;

    @Autowired
    public WaterBillController(WaterBillService waterBillService) {
        this.waterBillService = waterBillService;
    }

    // Create a new water bill
    @PostMapping
    public GenericResponse createWaterBill(@RequestBody WaterBillDTO waterBillDTO) {
        try {
            List<WaterBillEntity> waterBillEntities = waterBillRepository.findAllByRoomId(waterBillDTO.getRoom().getId());

            if (!waterBillEntities.isEmpty()) {
                for (WaterBillEntity waterBillEntity : waterBillEntities) {
                    if (waterBillEntity != null) {
                        int year = waterBillEntity.getCreateAt().getYear();
                        int month = waterBillEntity.getCreateAt().getMonthValue();

                        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
                        LocalDateTime to = from.plusMonths(1).minusNanos(1);
                        boolean exists = waterBillService.existsWaterBillForYearAndMonth(from, to);
                        if (exists) {
                            return new GenericResponse("Đã có hóa đơn tháng năm này");
                        } else {
                            waterBillService.createWaterBill(waterBillDTO);
                            return new GenericResponse("Tạo phiếu nước thành công");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse(e.getMessage());
        }

        waterBillService.createWaterBill(waterBillDTO);
        return new GenericResponse("Tạo phiếu nước thành công");
    }

    // Read all water bills
    @GetMapping
    public ResponseEntity<List<WaterBillDTO>> getAllWaterBills() {
        List<WaterBillDTO> waterBills = waterBillService.getAllWaterBills();
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<WaterBillDTO>> getAllWaterBillsByStudentId(@PathVariable Long id) {
        List<WaterBillDTO> waterBills = waterBillService.getAllWaterBillsByStudentId(id);
        return ResponseEntity.ok(waterBills);
    }

    // Read a water bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<WaterBillDTO> getWaterBillById(@PathVariable Long id) {
        WaterBillDTO waterBill = waterBillService.getWaterBillById(id);
        return ResponseEntity.ok(waterBill);
    }

    // Update a water bill
    @PutMapping("/{id}")
    public GenericResponse updateWaterBill(
            @PathVariable Long id, @RequestBody WaterBillDTO waterBillDTO) {
        int result = waterBillService.updateWaterBill(id, waterBillDTO);
        if (result == 0) {
            return new GenericResponse("Cập nhật hóa đơn thành công");
        } else if (result == 1) {
            return new GenericResponse("Hóa đơn đã thanh toán. Không thể cập nhật!");
        } else if (result == 3) {
            return new GenericResponse("Số nước phải lớn hơn 0");
        } else {
            return new GenericResponse("Hóa đơn không tồn tại");
        }
    }

    // Delete a water bill
    @DeleteMapping("/{id}")
    public GenericResponse deleteWaterBill(@PathVariable Long id) {
        return waterBillService.deleteWaterBill(id);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<?> payment(
            @PathVariable Long id) {
        int paymentWaterBill = waterBillService.paymentWaterBill(id);
        if (paymentWaterBill == 0) {
            return new ResponseEntity<>("Thanh toán thất bại", HttpStatus.BAD_REQUEST);
        } else if (paymentWaterBill == 2) {
            return new ResponseEntity<>("Hóa đơn đã thanh toán", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Thanh toán hóa đơn thành công", HttpStatus.OK);
    }
}