package com.ptithcm.onlinetest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/rooms", produces = "application/json")
public class RoomController {
//    @Autowired
//    private RoomTypesService roomTypesService;
//
//    @GetMapping
//    public ResponseEntity<List<RoomTypesDTO>> getAllRoomTypes() {
//        return new ResponseEntity<>(roomTypesService.findAll(), HttpStatus.OK);
//    }
}
