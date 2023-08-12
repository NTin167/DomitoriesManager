package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.InvoiceRepository;
import com.ptithcm.onlinetest.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ContractRepository contractRepository;

//    @GetMapping
//    public ResponseEntity<List<ContractDTO>> getAllContracts() {
//        List<ContractDTO> contracts = contractService.getAllContracts();
//        return new ResponseEntity<>(contracts, HttpStatus.OK);
//    }
    @GetMapping
    public List<ContractDTO> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable Long id) {
        try {
            List<ContractDTO> contract = contractService.getContractById(id);
            if (contract != null) {
                return new ResponseEntity<>(contract, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tồn tại hợp đồng với id = " + id ,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createContract(@RequestBody ContractDTO contract) {
        try {
            ContractDTO createdContract = contractService.addContract(contract);

            return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
        } catch (Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<?> changeStatusContractByContractId(@PathVariable Long id, @RequestParam(value="status", required=false) int status) {
        try {
            ContractEntity contract = contractService.changeStatus(id, status);
            if(contract != null) {
                return new ResponseEntity<>("Thay đổi trạng thái của hợp đồng thành công." ,HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Thay đổi trạng thái của hợp đồng thành công." ,HttpStatus.BAD_REQUEST);
    }
}