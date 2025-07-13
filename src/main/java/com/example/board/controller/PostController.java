package com.example.board.controller;

import com.example.board.dto.request.PostDto;
import com.example.board.dto.response.ResponsePostDto;
import com.example.board.service.PostService;
import com.example.board.service.RedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final RedisService redisService;
    // post 저장
    @PostMapping("/post")
    public ResponseEntity<String> save(@RequestBody @Valid PostDto dto) {
        postService.save(dto);
        return ResponseEntity.ok("저장 완료");
    }

    // post 단건 조회
    @GetMapping("/post/{id}")
    public ResponseEntity<ResponsePostDto> findById(@PathVariable Long id) {
        ResponsePostDto dto = postService.findById(id);
        redisService.incrementViewCount(id);
        return ResponseEntity.ok().body(dto);
    }

    //post page로 조회
    @GetMapping("/post/date")
    public ResponseEntity<List<ResponsePostDto>> findAll(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size
    ) {
        List<ResponsePostDto> pageList = postService.findByPage(size, page);
        return ResponseEntity.ok().body(pageList);
    }

    // post 인기글 조회
    @GetMapping("/post/top")
    public ResponseEntity<List<ResponsePostDto>> findTop() {
        List<ResponsePostDto> list = postService.getTopRankedPosts();
        return ResponseEntity.ok().body(list);
    }

    // post 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        postService.delete(id);
        redisService.removeView(id);

        return ResponseEntity.ok("삭제 완료");
    }

    // post 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<String> udpate(@PathVariable Long id, @RequestBody @Valid PostDto dto) {
        postService.update(id, dto);
        return ResponseEntity.ok("수정 완료");
    }
}
