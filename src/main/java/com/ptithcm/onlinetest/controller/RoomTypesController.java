package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.RoomTypesEntity;
import com.ptithcm.onlinetest.payload.dto.RoomTypesDTO;
import com.ptithcm.onlinetest.service.RoomTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtypes")
public class RoomTypesController {
    @Autowired
    private RoomTypesService roomTypesService;

    @PostMapping
    public ResponseEntity<RoomTypesEntity> createRoomType(@RequestBody RoomTypesDTO roomTypesDTO) {
        RoomTypesEntity roomTypesEntity = roomTypesService.createRoomType(roomTypesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomTypesEntity);
    }

    @GetMapping
    public ResponseEntity<List<RoomTypesDTO>> getAllRoomTypes() {
        List<RoomTypesDTO> roomTypesList = roomTypesService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypesList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypesEntity> getRoomTypeById(@PathVariable Long id) {
        RoomTypesEntity roomTypesEntity = roomTypesService.getRoomTypeById(id);
        if (roomTypesEntity != null) {
            return ResponseEntity.ok(roomTypesEntity);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable Long id, @RequestBody RoomTypesDTO roomTypesDTO) {
        RoomTypesDTO updatedRoomType = roomTypesService.updateRoomType(id, roomTypesDTO);
        if (updatedRoomType != null) {
            return ResponseEntity.ok(updatedRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypesService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}