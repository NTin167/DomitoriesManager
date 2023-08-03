package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.*;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class  ContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<ContractDTO> getAllContracts() {
        List<ContractEntity> contracts = contractRepository.findAll();
        return contracts.stream().map(this::mapToContractDTO).collect(Collectors.toList());
    }

    private ContractDTO mapToContractDTO(ContractEntity contractEntity) {
        ContractDTO contractDTO = new ContractDTO();
        // Map contractEntity properties to contractDTO
        contractDTO.setId(contractEntity.getId());
        contractDTO.setContractId(contractEntity.getContractId());
        contractDTO.setCreateAt(contractEntity.getCreateAt());
        contractDTO.setPrice(contractEntity.getPrice());
        contractDTO.setStatus(contractEntity.getStatus());
        contractDTO.setDateStart(contractEntity.getDateStart());
        contractDTO.setDateEnd(contractEntity.getDateEnd());
        contractDTO.setLeaseDuration(contractEntity.getLeaseDuration());
        if (contractEntity.getStaff() != null) {
            contractDTO.setStaffId(contractEntity.getStaff().getId());
        }
        if (contractEntity.getStudent() != null) {
            contractDTO.setStudentId(contractEntity.getStudent().getId());
        }
        if (contractEntity.getRoom() != null) {
            contractDTO.setRoomId(contractEntity.getRoom().getId());
        }
        if (contractEntity.getInvoices() != null) {
            contractDTO.setInvoiceIds(contractEntity.getInvoices().stream()
                    .map(InvoiceEntity::getId)
                    .collect(Collectors.toList()));
        }
        return contractDTO;
    }

    public ContractEntity getContractById(Long id) {
        Optional<ContractEntity> contract = contractRepository.findById(id);
        return contract.orElse(null);
    }

    public ContractDTO addContract(ContractDTO contractDTO) {
        ContractEntity contractEntity = mapToContractEntity(contractDTO);
        contractEntity = contractRepository.save(contractEntity);
        return mapToContractDTO(contractEntity);
    }
    private ContractEntity mapToContractEntity(ContractDTO contractDTO) {
        ContractEntity contractEntity = new ContractEntity();
        // Map contractDTO properties to contractEntity
        contractEntity.setContractId(contractDTO.getContractId());
        contractEntity.setCreateAt(contractDTO.getCreateAt());
        contractEntity.setPrice(contractDTO.getPrice());
        contractEntity.setStatus(contractDTO.getStatus());
        contractEntity.setDateStart(contractDTO.getDateStart());
        contractEntity.setDateEnd(contractDTO.getDateEnd());
        contractEntity.setLeaseDuration(contractDTO.getLeaseDuration());
        // Set other related entities like staff, student, room, and invoices if necessary
    // Fetch and set the associated staff entity (if staffId is provided)
        if (contractDTO.getStaffId() != null) {
            StaffEntity staffEntity = staffRepository.findById(contractDTO.getStaffId())
                    .orElseThrow(() -> new NoSuchElementException("Staff not found with id: " + contractDTO.getStaffId()));
            contractEntity.setStaff(staffEntity);
        }

        // Fetch and set the associated student entity (if studentId is provided)
        if (contractDTO.getStudentId() != null) {
            StudentEntity studentEntity = studentRepository.findById(contractDTO.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + contractDTO.getStudentId()));
            contractEntity.setStudent(studentEntity);
        }

        // Fetch and set the associated room entity (if roomId is provided)
        if (contractDTO.getRoomId() != null) {
            RoomEntity roomEntity = roomRepository.findById(contractDTO.getRoomId())
                    .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + contractDTO.getRoomId()));
            contractEntity.setRoom(roomEntity);
        }

        // Fetch and set the associated invoice entities (if invoiceIds are provided)
        if (contractDTO.getInvoiceIds() != null && !contractDTO.getInvoiceIds().isEmpty()) {
            List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllById(contractDTO.getInvoiceIds());
            contractEntity.setInvoices(invoiceEntities);
        }

        return contractEntity;
    }

    public ContractDTO updateContract(Long id, ContractDTO contractDTO) {
        ContractEntity existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contract not found with id: " + id));


        // Update the properties of existingContract with the values from contractDTO
        existingContract.setContractId(contractDTO.getContractId());
        existingContract.setCreateAt(contractDTO.getCreateAt());
        existingContract.setPrice(contractDTO.getPrice());
        existingContract.setStatus(contractDTO.getStatus());
        existingContract.setDateStart(contractDTO.getDateStart());
        existingContract.setDateEnd(contractDTO.getDateEnd());
        existingContract.setLeaseDuration(contractDTO.getLeaseDuration());
        // Update other related entities like staff, student, room, and invoices if necessary
        // Fetch and set the associated staff entity (if staffId is provided)
        if (contractDTO.getStaffId() != null) {
            StaffEntity staffEntity = staffRepository.findById(contractDTO.getStaffId())
                    .orElseThrow(() -> new NoSuchElementException("Staff not found with id: " + contractDTO.getStaffId()));
            existingContract.setStaff(staffEntity);
        }

        // Fetch and set the associated student entity (if studentId is provided)
        if (contractDTO.getStudentId() != null) {
            StudentEntity studentEntity = studentRepository.findById(contractDTO.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + contractDTO.getStudentId()));
            existingContract.setStudent(studentEntity);
        }

        // Fetch and set the associated room entity (if roomId is provided)
        if (contractDTO.getRoomId() != null) {
            RoomEntity roomEntity = roomRepository.findById(contractDTO.getRoomId())
                    .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + contractDTO.getRoomId()));
            existingContract.setRoom(roomEntity);
        }

        // Fetch and set the associated invoice entities (if invoiceIds are provided)
        if (contractDTO.getInvoiceIds() != null && !contractDTO.getInvoiceIds().isEmpty()) {
            List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllById(contractDTO.getInvoiceIds());
            existingContract.setInvoices(invoiceEntities);
        }


        contractRepository.save(existingContract);
        return mapToContractDTO(existingContract);
    }

    public void deleteContract(Long id) {
        ContractEntity existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contract not found with id: " + id));

        // Delete the contract
        contractRepository.delete(existingContract);
    }
}
