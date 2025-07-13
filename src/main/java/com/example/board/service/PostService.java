package com.example.board.service;

import com.example.board.dto.request.PostDto;
import com.example.board.dto.response.ResponsePostDto;
import com.example.board.entity.Post;
import com.example.board.entity.User;
import com.example.board.mapper.PostMapper;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    // 인기글 zset 키
    private static final String POST_VIEW_KEY = "post:view:";
    private static final String POST_RANKING_KEY = "post:ranking";

    // post 저장
    public void save(PostDto dto) {

        User user = userRepository.findById(dto.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // Mapper로 post 생성
        Post post = PostMapper.toEntity(dto, user);

        // DB에 post 저장
        postRepository.save(post);

        // 레디스 조회수 초기화
        redisTemplate.opsForValue().set(POST_VIEW_KEY + post.getId(), "0");

        // 레디스 랭킹 조회수 초기화
        redisTemplate.opsForZSet().add(POST_RANKING_KEY, String.valueOf(post.getId()), 0);
    }

    // post 단건 조회
    @Cacheable(value = "postCache", key = "#id")
    public ResponsePostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return PostMapper.toDto(post);
    }

    // post 페이지로 조회
    @Cacheable(cacheNames = "getPosts", key = "'posts:page:' + #page + ':size:' + #size")
    public List<ResponsePostDto> findByPage(int size, int page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return postPage.stream().map(PostMapper::toDto).collect(Collectors.toList());
    }

    // post 인기 10 조회
    public List<ResponsePostDto> getTopRankedPosts() {
        Set<String> postIdSet = redisTemplate.opsForZSet().reverseRange(POST_RANKING_KEY, 0, 9);
        if (postIdSet == null || postIdSet.isEmpty()) return Collections.emptyList();
        List<Post> posts = postRepository.findAllById(
                postIdSet.stream().map(Long::parseLong).collect(Collectors.toList())
        );
        return posts.stream().map(PostMapper::toDto).collect(Collectors.toList());

    }

    // post 삭제 + 캐시 무효화
    @Caching(evict = {
            @CacheEvict(value = "postCache", key = "#id"),
            @CacheEvict(cacheNames = "getPosts", allEntries = true)
    })
    public void delete(Long id) {
        postRepository.deleteById(id);
    }


    // post 수정 + 캐시 무효화
    @CacheEvict(value = "postCache", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "postCache", key = "#id"),
            @CacheEvict(cacheNames = "getPosts", allEntries = true)
    })
    public void update(Long id, PostDto dto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        post.update(dto.getTitle(), dto.getContents());
    }
}
