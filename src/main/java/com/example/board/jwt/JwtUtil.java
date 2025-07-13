package com.example.board.jwt;

import com.example.board.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "mySuperSecretKeyForJWTTokenGenerationExample"; // 256bit 이상 권장
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; //1 시간

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 토큰 발급
    public static String createToken(Long userId, String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role.name()) // enum -> 문자열로 저장
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // 토큰에서 정보 추출
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 Role 추출
    public static Role extractRole(String token) {
        Claims claims = parseToken(token);
        String roleStr = (String) claims.get("role");
        return Role.valueOf(roleStr);
    }
}
