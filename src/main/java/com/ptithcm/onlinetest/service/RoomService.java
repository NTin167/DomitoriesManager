package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.payload.dto.RoomDTO;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.RoomTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypesRepository roomTypesRepository;
    public RoomEntity createRoom(RoomEntity room) {
        return roomRepository.save(room);
    }


    public RoomEntity getRoomById(Long id) {
        Optional<RoomEntity> optionalRoom = roomRepository.findById(id);
        return optionalRoom.orElse(null);
    }

    public RoomEntity updateRoom(Long id, RoomEntity room) {
        RoomEntity existingRoom = getRoomById(id);
        if (existingRoom != null) {
            // Cập nhật thông tin phòng từ room được cung cấp
            existingRoom.setRoomName(room.getRoomName());
            existingRoom.setStatus(room.getStatus());
            existingRoom.setTotalCapacity(room.getTotalCapacity());
            existingRoom.setAvailableCapacity(room.getAvailableCapacity());
            existingRoom.setLinkImg(room.getLinkImg());
            if(roomTypesRepository.existsById(room.getRoomType().getId())) {
                existingRoom.setRoomType(room.getRoomType());
            }
            return roomRepository.save(existingRoom);
        } else {
            return null;
        }
    }

    public RoomDTO getRoom(Long id) {
        Optional<RoomEntity> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            return convertToDto(optionalRoom.get());
        }
        return null;
    }

    public boolean deleteRoom(Long id) {
        RoomEntity existingRoom = getRoomById(id);
        if (existingRoom != null) {
            roomRepository.delete(existingRoom);
            return true;
        } else {
            return false;
        }
    }

    public List<RoomDTO> getAllRooms() {
        List<RoomEntity> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RoomDTO convertToDto(RoomEntity room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setStatus(room.getStatus());
        roomDTO.setTotalCapacity(room.getTotalCapacity());
        roomDTO.setAvailableCapacity(room.getAvailableCapacity());
        roomDTO.setLinkImg(room.getLinkImg());
        if(room.getRoomType() != null) {
            roomDTO.setRoomTypeId(room.getRoomType().getId()); // Assuming RoomTypesEntity has getId() method
        }

        return roomDTO;
    }
}
