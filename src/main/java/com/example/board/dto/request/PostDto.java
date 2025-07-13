package com.example.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostDto {

    @NotBlank
    private String title;

    @NotBlank
    @Size(max=500)
    private String contents;

    private Long user_id;
}
