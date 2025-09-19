// controller/AuthController.java
package controller;

import lombok.RequiredArgsConstructor;
import model.dto.request.LoginRequest;
import model.dto.request.RegisterRequest;
import model.dto.response.ApiDataResponse;
import model.dto.response.JWTResponse;
import model.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<UserResponse>> register(@RequestBody RegisterRequest req) {
        ApiDataResponse<UserResponse> resp = authService.register(req);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JWTResponse>> login(@RequestBody LoginRequest req) {
        ApiDataResponse<JWTResponse> resp = authService.login(req);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiDataResponse<UserResponse>> me() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        ApiDataResponse<UserResponse> resp = authService.me(username);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }
}
