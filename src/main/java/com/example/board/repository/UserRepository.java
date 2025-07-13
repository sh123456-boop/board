package com.example.board.repository;

import com.example.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 닉네임 중복 검사
    boolean existsByUsername(String username);

    // 이메일 중복 검사
    boolean existsByEmail(String email);

    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);
}
