package edu.controller_advice;

import edu.model.dto.response.ApiDataResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiDataResponse.error(errors, "Dữ liệu không hợp lệ"));
    }
//
//    @ExceptionHandler(InvalidCredentialsException.class)
//    public ResponseEntity<ApiDataResponse<Object>> handleBadCredentials(InvalidCredentialsException ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(ApiDataResponse.error(ex.getMessage()));
//    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiDataResponse<Object>> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiDataResponse.error(e.getMessage()));
    }
//
//    @ExceptionHandler(ResourceConflictException.class)
//    public ResponseEntity<ApiDataResponse<Object>> handleResourceConflictException(ResourceConflictException e) {
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(ApiDataResponse.error(e.getMessage()));
//    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiDataResponse<Object>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiDataResponse.error(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiDataResponse<Object>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiDataResponse.error(e.getMessage()));
    }
}
