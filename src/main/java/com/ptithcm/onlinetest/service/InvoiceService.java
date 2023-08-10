package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.ContractEntity;
import com.ptithcm.onlinetest.entity.InvoiceEntity;
import com.ptithcm.onlinetest.payload.dto.ContractDTO;
import com.ptithcm.onlinetest.payload.dto.InvoiceDTO;
import com.ptithcm.onlinetest.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public InvoiceDTO getInvoiceById(Long id) {
        Optional<InvoiceEntity> invoiceOptional = invoiceRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            InvoiceEntity invoiceEntity = invoiceOptional.get();
            return convertToDTO(invoiceEntity);
        } else {
            return null;
        }
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




}
