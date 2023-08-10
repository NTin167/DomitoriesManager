package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.model.User;
import com.ptithcm.onlinetest.payload.dto.StaffDTO;
import com.ptithcm.onlinetest.payload.request.SignUpRequest;
import com.ptithcm.onlinetest.repository.StaffRepository;
import com.ptithcm.onlinetest.repository.UserRepository;
import com.ptithcm.onlinetest.service.StaffService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    StaffRepository staffRepository;

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

    @PostMapping("/registration")
    public GenericResponse registerUserAccount(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        System.out.println(signUpRequest.toString());
        if(userRepository.existsByUsername(signUpRequest.getUserName())) {
//            throw new UserAlreadyExistException("There is an account with that  username" + signUpRequest.getUserName());
            return new GenericResponse("There is an account with that  student code: " + signUpRequest.getUserName());
        }
        if(staffRepository.existsByStaffCode(signUpRequest.getUserName())) {
            return new GenericResponse("Staff code is null: " + signUpRequest.getUserName());
        }
        User registered = staffService.registerNewUserAccount(signUpRequest);
        return new GenericResponse("success");
    }
}

