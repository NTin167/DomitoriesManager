package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.RoomTypesEntity;
import com.ptithcm.onlinetest.payload.dto.RoomTypesDTO;
import com.ptithcm.onlinetest.repository.RoomTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypesService {
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    public RoomTypesEntity createRoomType(RoomTypesDTO roomTypesDTO) {
        RoomTypesEntity roomTypesEntity = new RoomTypesEntity();
        mapRoomTypeDTOToEntity(roomTypesDTO, roomTypesEntity);
        return roomTypesRepository.save(roomTypesEntity);
    }

    public List<RoomTypesEntity> getAllRoomTypes() {
        return roomTypesRepository.findAll();
    }

    public RoomTypesEntity getRoomTypeById(Long id) {
        return roomTypesRepository.findById(id).orElse(null);
    }

    public RoomTypesEntity updateRoomType(Long id, RoomTypesDTO roomTypesDTO) {
        RoomTypesEntity roomTypesEntity = roomTypesRepository.findById(id).orElse(null);
        if (roomTypesEntity != null) {
            mapRoomTypeDTOToEntity(roomTypesDTO, roomTypesEntity);
            return roomTypesRepository.save(roomTypesEntity);
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