package com.example.board.mapper;

import com.example.board.dto.request.SignupUserDto;
import com.example.board.dto.response.ResponseUserDto;
import com.example.board.entity.Post;
import com.example.board.entity.Role;
import com.example.board.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    // dto -> entity
    public static User toEntity(SignupUserDto dto, String password) {
        return User.builder()
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(password)
                .build();
    }

    // entity -> dto
    public static ResponseUserDto toDto(User user) {

        List<Post> posts = user.getPosts();
        List<Long> postId  = new ArrayList<>();
        for (Post post : posts) {
            postId.add(post.getId());
        }

        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .password(user.getPassword())
                .posts(postId)
                .build();
    }
}
