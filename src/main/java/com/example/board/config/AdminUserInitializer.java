package com.example.board.config;

import com.example.board.entity.Role;
import com.example.board.entity.User;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 패스워드 암호화용 (있으면)

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminEmail = "admin@example.com";

        // 이미 admin 계정이 존재하지 않는 경우만 생성
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin1234")) // 비밀번호 암호화 필수!
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("==== 관리자 계정 생성 완료 ====");
        } else {
            System.out.println("==== 관리자 계정이 이미 존재합니다 ====");
        }
    }
}
