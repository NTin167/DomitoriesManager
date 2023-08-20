package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ElectricBillEntity;
import com.ptithcm.onlinetest.payload.dto.ElectricBillDTO;
import com.ptithcm.onlinetest.repository.ElectricBillRepository;
import com.ptithcm.onlinetest.service.ElectricBillService;
import com.ptithcm.onlinetest.util.GenericResponse;
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
    public GenericResponse createElectricBill(@RequestBody ElectricBillDTO electricBillDTO) {
        try {
            List<ElectricBillEntity> electricBillEntities = electricBillRepository.findAllByRoomId(electricBillDTO.getRoom().getId());

            if (!electricBillEntities.isEmpty()) { // nếu đã có hóa đơn
                for (ElectricBillEntity electricBillEntity : electricBillEntities) {
                    if(electricBillEntity != null) {
                        int year = electricBillEntity.getCreateAt().getYear();
                        int month = electricBillEntity.getCreateAt().getMonthValue();

                        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
                        LocalDateTime to = from.plusMonths(1).minusNanos(1);
                        boolean exists = electricBillService.existsElectricBillForYearAndMonth(from, to);
                        if (exists) {
                            return new GenericResponse("Đã có hóa đơn tháng năm này");
                        } else {
                            electricBillService.createElectricBill(electricBillDTO);
                            return new GenericResponse("Tạo phiếu điện thành công");
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse(e.getMessage());
        }

        electricBillService.createElectricBill(electricBillDTO);
        return new GenericResponse("Tại phiếu điện thành công");
    }

    // Read all electric bills
    @GetMapping
    public ResponseEntity<List<ElectricBillDTO>> getAllElectricBills() {
        List<ElectricBillDTO> electricBills = electricBillService.getAllElectricBills();
        return ResponseEntity.ok(electricBills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ElectricBillDTO>> getAllElectricBillsByStudentId(@PathVariable Long id) {
        List<ElectricBillDTO> electricBills = electricBillService.getAllElectricBillsByStudentId(id);
        return ResponseEntity.ok(electricBills);
    }

    // Read an electric bill by ID
    @GetMapping("/student/{id}")
    public ResponseEntity<ElectricBillDTO> getElectricBillById(@PathVariable Long id) {
        ElectricBillDTO electricBill = electricBillService.getElectricBillById(id);
        return ResponseEntity.ok(electricBill);
    }

    // Update an electric bill
    @PutMapping("/{id}")
    public GenericResponse updateElectricBill(
            @PathVariable Long id, @RequestBody ElectricBillDTO electricBillDTO) {
        int result = electricBillService.updateElectricBill(id, electricBillDTO);
        if (result == 0) {// thanh cong
            return new GenericResponse("Cập nhật hóa đơn thành công");
        } else if(result == 1){
            // da thanh toan
            return new GenericResponse("Hóa đơn đã thanh toán. Không thể cập nhật!");
        } else if(result == 3){
            return new GenericResponse("Số điện phải lớn hơn 0");
        } else {
            return new GenericResponse("Hóa đơn không tồn tại");
        }
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