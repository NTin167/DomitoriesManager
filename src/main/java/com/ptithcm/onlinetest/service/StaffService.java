package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.StaffEntity;
import com.ptithcm.onlinetest.exception.AppException;
import com.ptithcm.onlinetest.model.Role;
import com.ptithcm.onlinetest.model.RoleName;
import com.ptithcm.onlinetest.model.User;
import com.ptithcm.onlinetest.payload.dto.StaffDTO;
import com.ptithcm.onlinetest.payload.request.SignUpRequest;
import com.ptithcm.onlinetest.repository.RoleRepository;
import com.ptithcm.onlinetest.repository.StaffRepository;
import com.ptithcm.onlinetest.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    public List<StaffDTO> getAllStaffs() {
        List<StaffEntity> staffEntities = staffRepository.findByDeleteYMDIsNull();
        return staffEntities.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public Optional<StaffDTO> getStaffById(Long id) {
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(id);
        return staffEntityOptional.map(this::convertEntityToDTO);
    }

    public StaffDTO createStaff(StaffDTO staffDTO) {
        StaffEntity staffEntity = convertDTOToEntity(staffDTO);
        if(!staffRepository.existsByStaffCode(staffEntity.getStaffCode())) {
            StaffEntity savedStaffEntity = staffRepository.save(staffEntity);
            return convertEntityToDTO(savedStaffEntity);
        } else {
            return null;
        }
    }

    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Optional<StaffEntity> staffOptional = staffRepository.findById(id);
        if(!staffRepository.existsByStaffCode(staffDTO.getStaffCode())) {
            if (staffOptional.isPresent()) {
                StaffEntity staffEntity = staffOptional.get();
                staffEntity.setStaffCode(staffDTO.getStaffCode());
                staffEntity.setGender(staffDTO.getGender());
                staffEntity.setDob(staffDTO.getDob());
                staffEntity.setName(staffDTO.getName());
                staffEntity.setPhoneNumber(staffDTO.getPhoneNumber());
                staffEntity.setEmail(staffDTO.getEmail());
                StaffEntity updatedStaffEntity = staffRepository.save(staffEntity);
                return convertEntityToDTO(updatedStaffEntity);
            }
        }
        return null;
    }

    public boolean deleteStaff(Long id) {
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(id);
        if (staffEntityOptional.isPresent() && staffEntityOptional.get().getDeleteYMD() == null) {
            StaffEntity staffEntity = staffEntityOptional.get();
            staffEntity.setDeleteYMD(LocalDate.now());
            staffRepository.save(staffEntity);
            return true;
        }
        return false;
    }

    private StaffDTO convertEntityToDTO(StaffEntity staffEntity) {
        StaffDTO staffDTO = new StaffDTO();
        BeanUtils.copyProperties(staffEntity, staffDTO);
        return staffDTO;
    }

    private StaffEntity convertDTOToEntity(StaffDTO staffDTO) {
        StaffEntity staffEntity = new StaffEntity();
        BeanUtils.copyProperties(staffDTO, staffEntity);
        return staffEntity;
    }

    public User registerNewUserAccount(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUserName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setEnabled(true);
        Role userRole = roleRepository.findByName(RoleName.ROLE_STAFF).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }
}