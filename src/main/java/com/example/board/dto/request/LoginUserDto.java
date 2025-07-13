package com.example.board.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginUserDto {

    //로그인 아이디
    @Email
    @NotBlank
    private String email;


    //로그인 비밀번호
    @NotBlank
    @Size(min = 8)
    private String password;
}
