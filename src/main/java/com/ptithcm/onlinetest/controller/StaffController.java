package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.payload.dto.StaffDTO;
import com.ptithcm.onlinetest.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaffs() {
        List<StaffDTO> staffDTOList = staffService.getAllStaffs();
        return new ResponseEntity<>(staffDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Long id) {
        Optional<StaffDTO> staffDTOOptional = staffService.getStaffById(id);
        return staffDTOOptional.map(staffDTO -> new ResponseEntity<>(staffDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createStaff(@RequestBody StaffDTO staffDTO) {
        StaffDTO createdStaff = staffService.createStaff(staffDTO);
        if(createdStaff != null){
            return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
        }
        return ResponseEntity.ok("MA NHAN VIEN DA TON TAI");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @RequestBody StaffDTO staffDTO) {
        StaffDTO updatedStaffOptional = staffService.updateStaff(id, staffDTO);
        if(updatedStaffOptional != null) {
            return new ResponseEntity<>(updatedStaffOptional, HttpStatus.OK);
        }
        return ResponseEntity.ok("MA NHAN VIEN DA TON TAI");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        boolean isDeleted = staffService.deleteStaff(id);
        return isDeleted ? ResponseEntity.ok("XOA THANH CONG")
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

