package com.example.board.service;

import com.example.board.dto.response.ResponseUserDto;
import com.example.board.entity.User;
import com.example.board.mapper.PostMapper;
import com.example.board.mapper.UserMapper;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 모든 유저 정보 확인
    public List<ResponseUserDto> userAll() {
        List<User> all = userRepository.findAll();

        return all.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}
