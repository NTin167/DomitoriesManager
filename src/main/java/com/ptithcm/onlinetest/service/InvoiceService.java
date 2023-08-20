package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.entity.InvoiceEntity;
import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.payload.dto.InvoiceDTO;
import com.ptithcm.onlinetest.repository.ContractRepository;
import com.ptithcm.onlinetest.repository.InvoiceRepository;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private RoomRepository roomRepository;
    public List<InvoiceDTO> getAllInvoicesByStudentId(Long studentId) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByContract_Student_Id(studentId);
        return invoiceEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InvoiceDTO> getAllInvoices() {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAll();
        return invoiceEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity invoiceEntity = convertToEntity(invoiceDTO);
        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);
        return convertToDTO(savedInvoice);
    }

    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Optional<InvoiceEntity> existingInvoiceOptional = invoiceRepository.findById(id);
        if (existingInvoiceOptional.isPresent()) {
            InvoiceEntity existingInvoice = existingInvoiceOptional.get();
            InvoiceEntity updatedInvoice = updateEntityFromDTO(existingInvoice, invoiceDTO);
            InvoiceEntity savedInvoice = invoiceRepository.save(updatedInvoice);
            return convertToDTO(savedInvoice);
        } else {
            return null;
        }
    }

    public boolean deleteInvoice(Long id) {
        Optional<InvoiceEntity> invoiceOptional = invoiceRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            invoiceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private InvoiceDTO convertToDTO(InvoiceEntity invoiceEntity) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceEntity.getId());
        invoiceDTO.setInvoiceId(invoiceEntity.getInvoiceId());
        invoiceDTO.setCreateAt(invoiceEntity.getCreateAt());
        invoiceDTO.setPrice(invoiceEntity.getPrice());
        invoiceDTO.setStatus(invoiceEntity.getStatus());
        invoiceDTO.setContract(convertToContractDTO(invoiceEntity.getContract()));
        invoiceDTO.getContract().setStaffId(invoiceEntity.getContract().getStaff().getId());
        invoiceDTO.getContract().setStaffName(invoiceEntity.getContract().getStaff().getName());
        invoiceDTO.getContract().setRoomId(invoiceEntity.getContract().getRoom().getId());
        invoiceDTO.getContract().setRoomName(invoiceEntity.getContract().getRoom().getRoomName());
        return invoiceDTO;
    }

    private InvoiceEntity convertToEntity(InvoiceDTO invoiceDTO) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(invoiceDTO.getId());
        invoiceEntity.setInvoiceId(invoiceDTO.getInvoiceId());
        invoiceEntity.setCreateAt(LocalDate.now());
        invoiceEntity.setPrice(invoiceDTO.getPrice());
        invoiceEntity.setStatus(invoiceDTO.getStatus());
        invoiceEntity.setContract(convertToContractEntity(invoiceDTO.getContract()));
        return invoiceEntity;
    }

    private InvoiceEntity updateEntityFromDTO(InvoiceEntity invoiceEntity, InvoiceDTO invoiceDTO) {
        invoiceEntity.setInvoiceId(invoiceDTO.getInvoiceId());
        invoiceEntity.setCreateAt(invoiceDTO.getCreateAt());
        invoiceEntity.setPrice(invoiceDTO.getPrice());
        invoiceEntity.setStatus(invoiceDTO.getStatus());
        invoiceEntity.setContract(convertToContractEntity(invoiceDTO.getContract()));
        return invoiceEntity;
    }

    private ContractDTO convertToContractDTO(ContractEntity contractEntity) {
        // Implement this conversion based on your ContractEntity and ContractDTO structures
        if (contractEntity == null) {
            return null;
        }

        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setId(contractEntity.getId());
        contractDTO.setContractId(contractEntity.getContractId());
        contractDTO.setCreateAt(contractEntity.getCreateAt());
        contractDTO.setPrice(contractEntity.getPrice());
        contractDTO.setStatus(contractEntity.getStatus());
        contractDTO.setDateStart(contractEntity.getDateStart());
        contractDTO.setDateEnd(contractEntity.getDateEnd());
        contractDTO.setLeaseDuration(contractEntity.getLeaseDuration());
        contractDTO.setStudentName(contractEntity.getStudent().getName());
        contractDTO.setStudentId(contractEntity.getStudent().getId());

        // You need to decide how to handle the StaffEntity, StudentEntity, and RoomEntity relationships here

        return contractDTO;
    }

    private ContractEntity convertToContractEntity(ContractDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(contractDTO.getId());
        contractEntity.setContractId(contractDTO.getContractId());
        contractEntity.setCreateAt(contractDTO.getCreateAt());
        contractEntity.setPrice(contractDTO.getPrice());
        contractEntity.setStatus(contractDTO.getStatus());
        contractEntity.setDateStart(contractDTO.getDateStart());
        contractEntity.setDateEnd(contractDTO.getDateEnd());
        contractEntity.setLeaseDuration(contractDTO.getLeaseDuration());

        // You need to decide how to handle the StaffDTO, StudentDTO, and RoomDTO relationships here

        return contractEntity;
    }


    public int paymentByInvoiceId(Long id) {
        Optional<InvoiceEntity> invoice =  invoiceRepository.findById(id);
        if (invoice.isPresent()) {
           if(invoice.get().getStatus() == 1) {
               return 0; // hóa đơn đã thanh toán
           } else {
               if (invoice.get().getContract().getRoom().getAvailableCapacity() > 0) {
                   invoice.get().setStatus(1);
                   Optional<ContractEntity> contract = contractRepository.findById(invoice.get().getContract().getId());
                   if(contract.isPresent()) {
                       contract.get().setStatus(3); // set trạng thái hợp đồng đã hoàn thành
                        Optional<RoomEntity> room = roomRepository.findById(invoice.get().getContract().getRoom().getId());
                        if (room.isPresent()) {
                            room.get().setAvailableCapacity(room.get().getAvailableCapacity() -  1);
                            invoiceRepository.save(invoice.get());
                            contractRepository.save(contract.get());
                            roomRepository.save(room.get());
                            return 1; // thành công
                        } else  {
                            return 2; // phòng không tồn tại
                        }

                   }
                   else  {
                       return 3; // hợp đồng không tồn tại
                   }

               } else {
                   return 4; // phòng đã hết chỗ không thể duyệt
               }
           }
        }
        return 5; // hóa đơn không tồn tại
    }
}
