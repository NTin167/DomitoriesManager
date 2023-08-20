package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.RoomTypesEntity;
import com.ptithcm.onlinetest.payload.dto.RoomTypesDTO;
import com.ptithcm.onlinetest.service.RoomTypesService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtypes")
public class RoomTypesController {
    @Autowired
    private RoomTypesService roomTypesService;

    @PostMapping
    public GenericResponse createRoomType(@RequestBody RoomTypesDTO roomTypesDTO) {
        int result  = roomTypesService.createRoomType(roomTypesDTO);
        if (result == 0 ){
            return new GenericResponse("Tạo mới loại phòng thành công");
        } else {
            return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
        }
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
    public GenericResponse updateRoomType(@PathVariable Long id, @RequestBody RoomTypesDTO roomTypesDTO) {
        int result = roomTypesService.updateRoomType(id, roomTypesDTO);
        if (result == 0) {
            return new GenericResponse("Sửa loại phòng thành công");
        } else if (result == 1) {
            return new GenericResponse("Loại phòng không tồn tại");
        } else if (result == 2) {
            return new GenericResponse("Loại phòng đã có phòng. Không thể sửa");
        }
        else {
            return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
        }
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteRoomType(@PathVariable Long id) {
        int result = roomTypesService.deleteRoomType(id);
        if(result == 0) {
            return new GenericResponse("Xóa thành công");
        } else if (result == 1 ) {
            return new GenericResponse("Loại phòng đã có phòng. Không thể xóa");
        } else if (result == 2) {
            return new GenericResponse("Phòng không tồn tại");
        } else {
            return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
        }
    }
}