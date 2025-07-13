package com.example.board.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 회원가입 관련 에러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> exception(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 유효성 검사
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(message);
    }

    // 필터 관련 에러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> Filter(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
