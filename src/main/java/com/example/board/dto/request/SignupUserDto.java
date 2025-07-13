package com.example.board.dto.request;

import com.example.board.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupUserDto {

    // 닉네임
    @NotBlank
    @Size(max = 15, message = "닉네임은 15자 이하여야 합니다.")
    private String username;

    //로그인 아이디
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank
    private String email;

    //로그인 비밀번호
    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상어야 합니다.")
    private String password;
}
