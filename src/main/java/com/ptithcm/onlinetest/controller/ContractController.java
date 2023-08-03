package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
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
    public ResponseEntity<ContractEntity> getContractById(@PathVariable Long id) {
        ContractEntity contract = contractService.getContractById(id);
        if (contract != null) {
            return new ResponseEntity<>(contract, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contract) {
        ContractDTO createdContract = contractService.addContract(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable Long id,
            @RequestBody ContractDTO updatedContract
    ) {
        ContractDTO contract = contractService.updateContract(id, updatedContract);
        if (contract != null) {
            return new ResponseEntity<>(contract, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}