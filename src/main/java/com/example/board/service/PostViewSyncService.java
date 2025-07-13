package com.example.board.service;

import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostViewSyncService {

    private final StringRedisTemplate redisTemplate;
    private final PostRepository postRepository;

    // 1분마다 실행
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void syncViewCountToDatabase() {
        ScanOptions options = ScanOptions.scanOptions().match("post:view:*").count(1000).build();
        try (Cursor<byte []> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options)) {
            while(cursor.hasNext()) {
                String key = new String(cursor.next());
                String[] split = key.split(":");
                if (split.length != 3) continue;
                long postId = Long.parseLong(split[2]);
                String value = redisTemplate.opsForValue().get(key);
                if (value == null) continue;
                Long delta = Long.parseLong(value);

                // 1. 증분만큼 db에 더하기 (jpql사용)
                int updatedRows = postRepository.incrementViews(postId, delta);
                System.out.println(">>> postId: " + postId + ", delta: " + delta + ", 업데이트 row: " + updatedRows);

                // 2. redis 값 초기화
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            System.err.println("syncViewCountToDatabase error: " + e.getMessage());        }
    }
}
