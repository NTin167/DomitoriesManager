package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.entity.RoomTypesEntity;
import com.ptithcm.onlinetest.payload.dto.RoomDTO;
import com.ptithcm.onlinetest.repository.RoomRepository;
import com.ptithcm.onlinetest.repository.RoomTypesRepository;
import com.ptithcm.onlinetest.util.GenericResponse;
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
    public GenericResponse createRoom(RoomEntity room) {
        if (room.getTotalCapacity() < 1) {
            return new GenericResponse("Số giường của phòng phải lớn hơn 1");
        } else  {
            Optional<RoomTypesEntity> roomTypesEntity = roomTypesRepository.findById(room.getRoomType().getId());
            if (roomTypesEntity.isPresent()) {
                roomRepository.save(room);
                return new GenericResponse("Thêm phòng thành công");
            } else {
                return new GenericResponse("Loại phòng không tồn tại");
            }
        }

    }


    public RoomEntity getRoomById(Long id) {
        Optional<RoomEntity> optionalRoom = roomRepository.findById(id);
        return optionalRoom.orElse(null);
    }

    public int updateRoom(Long id, RoomEntity room) {
        Optional<RoomEntity> existingRoom = roomRepository.findById(id);
        if (existingRoom.isPresent()) {
            if (existingRoom.get().getContracts().isEmpty()) {
                // Cập nhật thông tin phòng từ room được cung cấp
                existingRoom.get().setRoomName(room.getRoomName());
                existingRoom.get().setStatus(room.getStatus());
                existingRoom.get().setTotalCapacity(room.getTotalCapacity());
                existingRoom.get().setAvailableCapacity(room.getAvailableCapacity());
                existingRoom.get().setLinkImg(room.getLinkImg());
                if(roomTypesRepository.existsById(room.getRoomType().getId())) {
                    existingRoom.get().setRoomType(room.getRoomType());
                    roomRepository.save(existingRoom.get());
                    return 0; // thành công
                } else  {
                    return 3; // loại phòng k tồn tại
                }

            } else {
                return 2; // phòng đã có hợp đồng
            }

        } else {
            return 1; // phòng k tồn tại
        }
    }

    public RoomDTO getRoom(Long id) {
        Optional<RoomEntity> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            return convertToDto(optionalRoom.get());
        }
        return null;
    }

    public int deleteRoom(Long id) {
        RoomEntity existingRoom = getRoomById(id);
        if (existingRoom != null) {
            if(existingRoom.getContracts().isEmpty()) {
                roomRepository.delete(existingRoom);
                return 0;
            } else {
                return 1;
            }

        } else {
            return 2;
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
            roomDTO.setDescription(room.getRoomType().getDescription());
            roomDTO.setPrice(room.getRoomType().getPrice());
        }

        return roomDTO;
    }
}
