package com.example.board.controller;

import com.example.board.dto.request.LoginUserDto;
import com.example.board.dto.request.SignupUserDto;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SignController {

    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginUserDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok().body(token);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupUserDto dto) {
        userService.signUp(dto);
        return ResponseEntity.ok("회원가입 완료");
    }
}
