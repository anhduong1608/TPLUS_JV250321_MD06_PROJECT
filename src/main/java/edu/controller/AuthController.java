package edu.controller;

import edu.model.dto.response.ApiDataResponse;
import edu.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JwtResponse>> login(@RequestBody UserLogin userLogin) {
        JwtResponse jwtResponse = authService.login(userLogin);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiDataResponse.success(jwtResponse, "Đăng nhập thành công"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<Users>> register(@Valid @RequestBody UserRegister userRegister) {
        Users registeredUser = authService.register(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiDataResponse.success(registeredUser, "Đăng ký tài khoản thành công"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiDataResponse<Users>> getCurrentUser() {
        Users currentUser = authService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiDataResponse.success(currentUser, "Lấy thông tin người dùng hiện tại thành công"));
    }
}
