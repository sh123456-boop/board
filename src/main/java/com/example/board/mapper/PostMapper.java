package com.example.board.mapper;

import com.example.board.dto.request.PostDto;
import com.example.board.dto.response.ResponsePostDto;
import com.example.board.entity.Post;
import com.example.board.entity.User;

import java.time.LocalDateTime;

public class PostMapper {

    // dto -> entity
    public static Post toEntity(PostDto dto, User user) {
        return Post.builder()
                .user(user)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // entity -> dto
    public static ResponsePostDto toDto(Post post) {
        return ResponsePostDto.builder()
                .id(post.getId())
                .contents(post.getContents())
                .title(post.getTitle())
                .views(post.getViews())
                .username(post.getUser().getUsername())
                .userId(post.getUser().getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
