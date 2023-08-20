package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.entity.RoomEntity;
import com.ptithcm.onlinetest.payload.dto.RoomDTO;
import com.ptithcm.onlinetest.service.RoomService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // API tạo mới một phòng
    @PostMapping
    public GenericResponse createRoom(@RequestBody RoomEntity room) {
        return roomService.createRoom(room);
    }

    // API lấy thông tin tất cả phòng
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    // API lấy thông tin một phòng theo id
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoom(id);
        if (room != null) {
            return new ResponseEntity<>(room, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // API cập nhật thông tin một phòng
    @PutMapping("/{id}")
    public GenericResponse updateRoom(@PathVariable Long id, @RequestBody RoomEntity room) {
        int result = roomService.updateRoom(id, room);
        if (result == 0) {
            return new GenericResponse("Sửa phòng thành công");
        } else if  (result == 1){
            return new GenericResponse("Phòng không tồn tại");
        } else if  (result == 2){
            return new GenericResponse("Phòng đã có đăng ký. Sửa phòng thất bại");
        } else if  (result == 3){
            return new GenericResponse("Loại phòng không tồn tại");
        } else {
            return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
        }
    }

    // API xóa một phòng
    @DeleteMapping("/{id}")
    public GenericResponse deleteRoom(@PathVariable Long id) {
        int result = roomService.deleteRoom(id);
        if (result == 0) {
            return new GenericResponse("Xóa phòng thành công");
        } else if (result == 1 ){
            return new GenericResponse("Phòng đã có người đăng ký. Không thể xóa!");
        } else if (result == 2 ){
            return new GenericResponse("Phòng không tồn tại");
        }
        return new GenericResponse("Đã xảy ra lỗi trong quá trình xử lý");
    }
}
