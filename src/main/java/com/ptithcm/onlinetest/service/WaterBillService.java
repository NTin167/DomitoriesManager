package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.entity.StudentEntity;
import com.ptithcm.onlinetest.entity.WaterBillEntity;
import com.ptithcm.onlinetest.payload.dto.WaterBillDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.StudentRepository;
import com.ptithcm.onlinetest.repository.WaterBillRepository;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaterBillService {

    private final WaterBillRepository waterBillRepository;
    private final ModelMapper modelMapper;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    public WaterBillService(WaterBillRepository waterBillRepository, ModelMapper modelMapper) {
        this.waterBillRepository = waterBillRepository;
        this.modelMapper = modelMapper;
    }

    public WaterBillDTO createWaterBill(WaterBillDTO waterBillDTO) {
        WaterBillEntity waterBillEntity = new WaterBillEntity();
        waterBillEntity.setCreateAt(LocalDateTime.now());
        waterBillEntity.setStatus(0);
        waterBillEntity.setRoom(roomRepository.findById(waterBillDTO.getRoom().getId()).get());
        waterBillEntity.setPrice(waterBillDTO.getPrice());
        waterBillEntity.setWaterNumber(waterBillDTO.getWaterNumber());
        waterBillEntity = waterBillRepository.save(waterBillEntity);
        return modelMapper.map(waterBillEntity, WaterBillDTO.class);
    }

    public List<WaterBillDTO> getAllWaterBills() {
        List<WaterBillEntity> waterBillEntities = waterBillRepository.findAll();
        return waterBillEntities.stream()
                .map(waterBillEntity -> modelMapper.map(waterBillEntity, WaterBillDTO.class))
                .collect(Collectors.toList());
    }

    public List<WaterBillDTO> getAllWaterBillsByStudentId(Long studentId) {
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            for (ContractEntity contract : student.get().getContracts()) {
                if (contract.getExpiryStatus() == 0) {
                    List<WaterBillEntity> waterBillEntities = waterBillRepository.findAllByRoomId(contract.getRoom().getId());
                    return waterBillEntities.stream()
                            .map(waterBillEntity -> modelMapper.map(waterBillEntity, WaterBillDTO.class))
                            .collect(Collectors.toList());
                }
            }
        }
        return null;
    }

    public WaterBillDTO getWaterBillById(Long id) {
        Optional<WaterBillEntity> waterBillEntityOptional = waterBillRepository.findById(id);
        if (waterBillEntityOptional.isPresent()) {
            return modelMapper.map(waterBillEntityOptional.get(), WaterBillDTO.class);
        } else {
            throw new RuntimeException("Water bill not found with id: " + id);
        }
    }

    public int updateWaterBill(Long id, WaterBillDTO waterBillDTO) {
        Optional<WaterBillEntity> waterBillEntityOptional = waterBillRepository.findById(id);
        if (waterBillEntityOptional.isPresent()) {
            if (waterBillEntityOptional.get().getStatus() == 1) {
                return 1;
            } else {
                WaterBillEntity updatedWaterBillEntity = waterBillEntityOptional.get();
                Optional<RoomEntity> room = roomRepository.findById(waterBillDTO.getRoom().getId());
                room.ifPresent(updatedWaterBillEntity::setRoom);
                if (waterBillDTO.getPrice() > 0 && waterBillDTO.getWaterNumber() > 0) {
                    updatedWaterBillEntity.setPrice(waterBillDTO.getPrice());
                    updatedWaterBillEntity.setWaterNumber(waterBillDTO.getWaterNumber());
                    waterBillRepository.save(updatedWaterBillEntity);
                    return 0;
                }
                return 3;
            }
        } else {
            return 2;
        }
    }

    public GenericResponse deleteWaterBill(Long id) {
        Optional<WaterBillEntity> waterBillEntityOptional = waterBillRepository.findById(id);
        if (waterBillEntityOptional.isPresent()) {
            if (waterBillEntityOptional.get().getStatus() == 1) {
                return new GenericResponse("Phiếu nước đã thanh toán. Không thể xóa");
            } else {
                waterBillRepository.deleteById(id);
                return new GenericResponse("Xóa phiếu nước thành công");
            }
        } else {
            return new GenericResponse("Phiếu nước không tồn tại");
        }
    }

    public boolean existsWaterBillForYearAndMonth(LocalDateTime from, LocalDateTime to) {
        return waterBillRepository.existsByCreateAtBetween(from, to);
    }

    public int paymentWaterBill(Long id) {
        Optional<WaterBillEntity> waterBillEntity = waterBillRepository.findById(id);
        if (waterBillEntity.isPresent()) {
            if (waterBillEntity.get().getStatus() == 1) {
                return 2;
            } else {
                waterBillEntity.get().setStatus(1);
                waterBillRepository.save(waterBillEntity.get());
                return 1;
            }
        } else {
            return 0;
        }
    }
}