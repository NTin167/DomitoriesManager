package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.payload.dto.RoomDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.InvoiceRepository;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.service.ContractService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private RoomRepository roomRepository;
//    @GetMapping
//    public ResponseEntity<List<ContractDTO>> getAllContracts() {
//        List<ContractDTO> contracts = contractService.getAllContracts();
//        return new ResponseEntity<>(contracts, HttpStatus.OK);
//    }
    @GetMapping
    public List<ContractDTO> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getContractByStudentId(@PathVariable Long studentId) {
        try {
            List<ContractDTO> contract = contractService.getContractById(studentId);
            if (contract != null) {
                return new ResponseEntity<>(contract, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tồn tại hợp đồng với id = " + studentId ,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public GenericResponse createContract(@RequestBody ContractDTO contract) {
        try {
            int i  = contractService.addContract(contract);
            if ( i == 0 ) {
                return new GenericResponse("Đăng ký phòng thành công");
            } else if ( i == 1 ) {
                return new GenericResponse("Phòng đã hết chỗ");
            } else if ( i == 2) {
                return new GenericResponse("Phòng không tồn tại");
            }
        } catch (Exception e ) {
            e.printStackTrace();
            return new GenericResponse("Đăng ký phòng thất bại");
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContract(
            @PathVariable Long id,
            @RequestBody ContractDTO updatedContract
    ) {
        try {
            ContractDTO contract = contractService.updateContract(id, updatedContract);
            if (contract != null) {
                return new ResponseEntity<>(contract, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không có hợp đồng với id này" ,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/changeStatus/{id}")
    public GenericResponse changeStatusContractByContractId(@PathVariable Long id,
                                                              @RequestParam(value="status", required=false) int status,
                                                              @RequestParam(value = "staffId", required = false) Long staffId) {
        try {
            int result = contractService.changeStatus(id, status, staffId);
            if(result == 0) {
                return new GenericResponse("Thay đổi trạng thái của hợp đồng thành công.");
            } else if (result == 1) {
                return new GenericResponse("Trạng thái của hợp đồng không thể thay đổi.");
            } else if (result == 2) {
                return new GenericResponse("Hợp đồng không tồn tại.");
            }
            else {
                return new GenericResponse("Thay đổi trạng thái của hợp đồng thất bại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse(e.getMessage());
        }
    }

    @GetMapping("/getInfoRoom/{contractId}")
    public ResponseEntity<?> getInfoRoom(@PathVariable Long contractId) {
        RoomDTO roomDTO = new RoomDTO();
        Optional<RoomEntity> room = roomRepository.findById(contractId);
        if (room.isPresent()) {
            roomDTO.setId(room.get().getId());
            roomDTO.setRoomName(room.get().getRoomName());
            roomDTO.setRoomTypeName(room.get().getRoomType().getName());
            roomDTO.setAvailableCapacity(room.get().getAvailableCapacity());
            roomDTO.setTotalCapacity(room.get().getTotalCapacity());
            roomDTO.setDescription(room.get().getRoomType().getDescription());

            return new ResponseEntity<>(roomDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Khong co phong", HttpStatus.BAD_REQUEST);
    }
}