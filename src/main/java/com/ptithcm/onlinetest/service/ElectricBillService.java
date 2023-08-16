package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.entity.ElectricBillEntity;
import com.ptithcm.onlinetest.entity.StudentEntity;
import com.ptithcm.onlinetest.payload.dto.ElectricBillDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.ElectricBillRepository;
import com.ptithcm.onlinetest.repository.StudentRepository;
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
    public ElectricBillService(ElectricBillRepository electricBillRepository, ModelMapper modelMapper) {
        this.electricBillRepository = electricBillRepository;
        this.modelMapper = modelMapper;
    }

    public ElectricBillDTO createElectricBill(ElectricBillDTO electricBillDTO) {
        ElectricBillEntity electricBillEntity = modelMapper.map(electricBillDTO, ElectricBillEntity.class);
        electricBillEntity.setCreateAt(LocalDateTime.now());
        electricBillEntity.setStatus(0);
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
                if(contract.getExpiryStatus() == 0) {
                    List<ElectricBillEntity> electricBillEntities = electricBillRepository.findAllByRoomId(contract.getRoom().getId());
                    return electricBillEntities.stream()
                            .map(electricBillEntity -> modelMapper.map(electricBillEntity, ElectricBillDTO.class))
                            .collect(Collectors.toList());

                }
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

    public ElectricBillDTO updateElectricBill(Long id, ElectricBillDTO electricBillDTO) {
        Optional<ElectricBillEntity> electricBillEntityOptional = electricBillRepository.findById(id);
        if (electricBillEntityOptional.isPresent()) {
            ElectricBillEntity updatedElectricBillEntity = electricBillEntityOptional.get();
            modelMapper.map(electricBillDTO, updatedElectricBillEntity);
            updatedElectricBillEntity = electricBillRepository.save(updatedElectricBillEntity);
            return modelMapper.map(updatedElectricBillEntity, ElectricBillDTO.class);
        } else {
            throw new RuntimeException("Electric bill not found with id: " + id);
        }
    }

    public void deleteElectricBill(Long id) {
        electricBillRepository.deleteById(id);
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