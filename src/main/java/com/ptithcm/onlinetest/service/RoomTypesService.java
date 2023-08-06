package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.entity.RoomTypesEntity;
import com.ptithcm.onlinetest.payload.dto.RoomDTO;
import com.ptithcm.onlinetest.payload.dto.RoomTypesDTO;
import com.ptithcm.onlinetest.repository.RoomTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypesService {
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    public RoomTypesEntity createRoomType(RoomTypesDTO roomTypesDTO) {
        RoomTypesEntity roomTypesEntity = new RoomTypesEntity();
        mapRoomTypeDTOToEntity(roomTypesDTO, roomTypesEntity);
        return roomTypesRepository.save(roomTypesEntity);
    }

    public List<RoomTypesDTO> getAllRoomTypes() {
        List<RoomTypesEntity> roomTypes = roomTypesRepository.findAll();
        return roomTypes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private RoomTypesDTO convertToDto(RoomTypesEntity roomType) {
        RoomTypesDTO roomTypeDTO = new RoomTypesDTO();
        roomTypeDTO.setId(roomType.getId());
        roomTypeDTO.setBedNumber(roomType.getBedNumber());
        roomTypeDTO.setImage(roomType.getImage());
        roomTypeDTO.setPrice(roomType.getPrice());
        roomTypeDTO.setName(roomType.getName());
        roomTypeDTO.setRoomGender(roomType.getRoomGender());
        roomTypeDTO.setDescription(roomType.getDescription());
        // Convert List<RoomEntity> to List<RoomDTO>
        List<RoomDTO> roomDTOList = new ArrayList<>();

        for (RoomEntity roomEntity : roomType.getRooms()) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(roomEntity.getId());
            roomDTO.setRoomName(roomEntity.getRoomName());
            roomDTO.setStatus(roomEntity.getStatus());
            roomDTO.setRoomTypeId(roomEntity.getRoomType().getId());
            roomDTO.setTotalCapacity(roomEntity.getTotalCapacity());
            roomDTO.setLinkImg(roomEntity.getLinkImg());
            roomDTO.setAvailableCapacity(roomEntity.getAvailableCapacity());
            roomDTOList.add(roomDTO);
        }
        if ( roomDTOList.size() > 0)
            roomTypeDTO.setRoomDTOS(roomDTOList);

        return roomTypeDTO;
    }

    public RoomTypesEntity getRoomTypeById(Long id) {
        return roomTypesRepository.findById(id).orElse(null);
    }

    public RoomTypesDTO updateRoomType(Long id, RoomTypesDTO roomTypesDTO) {
        RoomTypesEntity roomTypesEntity = roomTypesRepository.findById(id).orElse(null);
        if (roomTypesEntity != null) {
            mapRoomTypeDTOToEntity(roomTypesDTO, roomTypesEntity);
            roomTypesRepository.save(roomTypesEntity);
            return roomTypesDTO;
        }
        return null;
    }

    public void deleteRoomType(Long id) {
        roomTypesRepository.deleteById(id);
    }

    // Helper method to map RoomTypesDTO to RoomTypesEntity
    private void mapRoomTypeDTOToEntity(RoomTypesDTO roomTypesDTO, RoomTypesEntity roomTypesEntity) {
        roomTypesEntity.setBedNumber(roomTypesDTO.getBedNumber());
        roomTypesEntity.setImage(roomTypesDTO.getImage());
        roomTypesEntity.setPrice(roomTypesDTO.getPrice());
        roomTypesEntity.setName(roomTypesDTO.getName());
        roomTypesEntity.setRoomGender(roomTypesDTO.getRoomGender());
        roomTypesEntity.setDescription(roomTypesDTO.getDescription());
    }
}