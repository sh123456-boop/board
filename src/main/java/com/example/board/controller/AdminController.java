package com.example.board.controller;

import com.example.board.dto.response.ResponseUserDto;
import com.example.board.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<ResponseUserDto>> findAllUser() {
        List<ResponseUserDto> list = adminService.userAll();
        return ResponseEntity.ok().body(list);
    }
}