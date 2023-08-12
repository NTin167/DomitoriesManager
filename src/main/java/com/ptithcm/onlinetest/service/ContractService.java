package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.*;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if(contractEntity.getDateEnd() != null) {
            int i = contractEntity.getDateEnd().compareTo(LocalDate.now());
            if (i <= 0) {
                contractDTO.setExpiryStatus(1);
                contractEntity.setExpiryStatus(1);
                contractRepository.save(contractEntity);
            } else {
                contractDTO.setExpiryStatus(0);
                contractEntity.setExpiryStatus(0);
            }
        }



        if (contractEntity.getStaff() != null) {
            contractDTO.setStaffId(contractEntity.getStaff().getId());
            contractDTO.setStaffName(contractEntity.getStaff().getName());
        }
        if (contractEntity.getStudent() != null) {
            contractDTO.setStudentId(contractEntity.getStudent().getId());
            contractDTO.setStudentName(contractEntity.getStudent().getName());
        }
        if (contractEntity.getRoom() != null) {
            contractDTO.setRoomId(contractEntity.getRoom().getId());
            contractDTO.setRoomName(contractEntity.getRoom().getRoomName());
        }
        if (contractEntity.getInvoices() != null) {
            contractDTO.setInvoiceIds(contractEntity.getInvoices().stream()
                    .map(InvoiceEntity::getId)
                    .collect(Collectors.toList()));
        }
        return contractDTO;
    }

    public List<ContractDTO> getContractById(Long id) {
        List<ContractEntity> contract = contractRepository.findAllByStudentId(id);
        if  ( contract.isEmpty())  {
            return null;
        }
        return contract.stream().map(this::mapToContractDTO).collect(Collectors.toList());
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
        contractEntity.setCreateAt(LocalDate.now());
        contractEntity.setPrice(contractDTO.getPrice());
        contractEntity.setStatus(0); // lúc mới đăng ký trạng thái 0 là chưa duyệt
        contractEntity.setDateStart(contractDTO.getDateStart());
        contractEntity.setDateEnd(contractDTO.getDateEnd());
        contractEntity.setLeaseDuration(contractDTO.getLeaseDuration());
        int i = contractDTO.getDateEnd().compareTo(LocalDate.now());
        if (i <= 0) {
            contractEntity.setExpiryStatus(1);
        } else {
            contractEntity.setExpiryStatus(0);
        }

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

    public ContractEntity changeStatus (Long id, int status) {
        ContractEntity existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contract not found with id: " + id));
        if (status == 1) {
            if ( existingContract != null) {
                InvoiceEntity invoice = new InvoiceEntity();
                invoice.setCreateAt(LocalDate.now());
                invoice.setStatus(0);
                invoice.setPrice(existingContract.getPrice());
                Optional<ContractEntity> contractEntity1 = contractRepository.findById(existingContract.getId());
                if(contractEntity1.isPresent()) {
                    invoice.setContract(contractEntity1.get());
                }
                else  {
                    return null;
                }
                invoiceRepository.save(invoice);

            }

            RoomEntity room = roomRepository.findById(existingContract.getRoom().getId()).get();
            room.setAvailableCapacity(room.getAvailableCapacity() - 1 );
            roomRepository.save(room);
        }
        existingContract.setStatus(status);
        contractRepository.save(existingContract);
        return existingContract;
    }
}
