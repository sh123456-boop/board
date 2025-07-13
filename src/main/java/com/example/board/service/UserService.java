package com.example.board.service;

import com.example.board.dto.request.LoginUserDto;
import com.example.board.dto.request.SignupUserDto;
import com.example.board.entity.Role;
import com.example.board.entity.User;
import com.example.board.jwt.JwtUtil;
import com.example.board.mapper.UserMapper;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // 회원가입 기능
    public void signUp(SignupUserDto dto) {

        // 닉네임 중복 검증
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // email 중복 검증
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // password 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // Mapper로 user 빌드
        User user = UserMapper.toEntity(dto, encodedPassword);

        // 저장
        userRepository.save(user);
    }

    // 로그인 기능
    public String login(LoginUserDto dto) {
        // 이메일로 사용자 찾기
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // jwt 토큰 발급
        return JwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
    }
}
