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
    public GenericResponse createStaff(@RequestBody StaffDTO staffDTO) {
        int result = staffService.createStaff(staffDTO);
        if(result == 0){
            return new GenericResponse("Thêm nhân viên thành công");
        } else {
            return new GenericResponse("Mã nhân viên đã tồn tại. Thêm nhân viên thất bại!");
        }
    }

    @PutMapping("/{id}")
    public GenericResponse updateStaff(@PathVariable Long id, @RequestBody StaffDTO staffDTO) {
        int result = staffService.updateStaff(id, staffDTO);
        if(result == 0) {
            return new GenericResponse("Sửa nhân viên thàn công");
        } else if (result == 1) {
            return new GenericResponse("Nhân viên không tồn tại");
        } else {
            return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
        }
    }


    @DeleteMapping("/{id}")
    public GenericResponse deleteStaff(@PathVariable Long id) {
        return staffService.deleteStaff(id);

    }

    @PostMapping("/registration")
    public GenericResponse registerUserAccount(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        System.out.println(signUpRequest.toString());
        if(userRepository.existsByUsername(signUpRequest.getUserName())) {
//            throw new UserAlreadyExistException("There is an account with that  username" + signUpRequest.getUserName());
            return new GenericResponse("There is an account with that  student code: " + signUpRequest.getUserName());
        }
        if(!staffRepository.existsByStaffCode(signUpRequest.getUserName())) {
            return new GenericResponse("Staff code is null: " + signUpRequest.getUserName());
        }
        User registered = staffService.registerNewUserAccount(signUpRequest);
        return new GenericResponse("success");
    }
}

