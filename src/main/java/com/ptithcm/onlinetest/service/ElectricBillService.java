package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.entity.ElectricBillEntity;
import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.entity.StudentEntity;
import com.ptithcm.onlinetest.payload.dto.ElectricBillDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.ElectricBillRepository;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.StudentRepository;
 import com.ptithcm.onlinetest.util.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElectricBillService {

    private final ElectricBillRepository electricBillRepository;
    private final ModelMapper modelMapper;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    public ElectricBillService(ElectricBillRepository electricBillRepository, ModelMapper modelMapper) {
        this.electricBillRepository = electricBillRepository;
        this.modelMapper = modelMapper;
    }

    public ElectricBillDTO createElectricBill(ElectricBillDTO electricBillDTO) {
        ElectricBillEntity electricBillEntity = new ElectricBillEntity();
        electricBillEntity.setCreateAt(LocalDateTime.now());
        electricBillEntity.setStatus(0);
        electricBillEntity.setRoom(roomRepository.findById(electricBillDTO.getRoom().getId()).get());
        electricBillEntity.setPrice(electricBillDTO.getPrice());
        electricBillEntity.setElectricNumber(electricBillDTO.getElectricNumber());
        electricBillEntity = electricBillRepository.save(electricBillEntity);
        return modelMapper.map(electricBillEntity, ElectricBillDTO.class);
    }

    public List<ElectricBillDTO> getAllElectricBills() {
        List<ElectricBillEntity> electricBillEntities = electricBillRepository.findAll();
        return electricBillEntities.stream()
                .map(electricBillEntity -> modelMapper.map(electricBillEntity, ElectricBillDTO.class))
                .collect(Collectors.toList());
    }
    public List<ElectricBillDTO> getAllElectricBillsByStudentId(Long studentId) {
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if(student.isPresent()) {
            for (ContractEntity contract : student.get().getContracts()) {
                    List<ElectricBillEntity> electricBillEntities = electricBillRepository.findAllByRoomId(contract.getRoom().getId());
                    return electricBillEntities.stream()
                            .map(electricBillEntity -> modelMapper.map(electricBillEntity, ElectricBillDTO.class))
                            .collect(Collectors.toList());

            }
        }
        return null;
    }

    public ElectricBillDTO getElectricBillById(Long id) {
        Optional<ElectricBillEntity> electricBillEntityOptional = electricBillRepository.findById(id);
        if (electricBillEntityOptional.isPresent()) {
            return modelMapper.map(electricBillEntityOptional.get(), ElectricBillDTO.class);
        } else {
            throw new RuntimeException("Electric bill not found with id: " + id);
        }
    }

    public int updateElectricBill(Long id, ElectricBillDTO electricBillDTO) {
        Optional<ElectricBillEntity> electricBillEntityOptional = electricBillRepository.findById(id);
        if (electricBillEntityOptional.isPresent()) {
            if (electricBillEntityOptional.get().getStatus() == 1) {
                return 1;
            } else {
                ElectricBillEntity updatedElectricBillEntity = electricBillEntityOptional.get();
                Optional<RoomEntity> room = roomRepository.findById(electricBillDTO.getRoom().getId());
                room.ifPresent(updatedElectricBillEntity::setRoom);
                if (electricBillDTO.getPrice() > 0 && electricBillDTO.getElectricNumber() > 0) {
                    updatedElectricBillEntity.setPrice(electricBillDTO.getPrice());
                    updatedElectricBillEntity.setElectricNumber(electricBillDTO.getElectricNumber());
                    electricBillRepository.save(updatedElectricBillEntity);
                    return 0;
                }
                return 3;

            }

        } else {
            return 2;
        }
    }

    public GenericResponse deleteElectricBill(Long id) {
        Optional<ElectricBillEntity> electricBillEntityOptional = electricBillRepository.findById(id);
        if (electricBillEntityOptional.isPresent()) {
            if (electricBillEntityOptional.get().getStatus() == 1) {
                return new GenericResponse("Phiếu điện đã thanh toán. Không thể xóa");
            } else {
                electricBillRepository.deleteById(id);
                return new GenericResponse("Xóa phiếu điện thành công");
            }
        } else {
            return new GenericResponse("Phiếu điện không tồn tại");
        }
    }


    public boolean existsElectricBillForYearAndMonth(LocalDateTime from, LocalDateTime to) {
        return electricBillRepository.existsByCreateAtBetween(from, to);
    }

    public int paymentElectricBill(Long id) {
        Optional<ElectricBillEntity> electricBillEntity = electricBillRepository.findById(id);
        if (electricBillEntity.isPresent()) {
            if(electricBillEntity.get().getStatus() == 1) {
                return 2;
            }
            else  {
                electricBillEntity.get().setStatus(1);
                electricBillRepository.save(electricBillEntity.get());
                return 1;
            }
        } else  {
            return 0;
        }
    }

}