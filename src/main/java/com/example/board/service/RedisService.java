package com.example.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // 조회수 증가
    public void incrementViewCount(Long id) {
        // 일반 게시물 조회수 증가
        String key = "post:view:" + id;
        redisTemplate.opsForValue().increment(key, 1);

        // 인기글 조회수 증가
        String zset_key = "post:ranking";
        redisTemplate.opsForZSet().incrementScore(zset_key, String.valueOf(id), 1);
    }

    // 조회수 삭제
    public void removeView(Long id) {
        String key = "post:view" + id;
        redisTemplate.delete(key);

        String zset_key = "post:ranking";
        redisTemplate.opsForZSet().remove(zset_key, String.valueOf(id));


    }
}
