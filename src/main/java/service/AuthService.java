// service/AuthService.java
package service;

import model.dto.request.LoginRequest;
import model.dto.request.RegisterRequest;
import model.dto.response.ApiDataResponse;
import model.dto.response.JWTResponse;
import model.dto.response.UserResponse;

public interface AuthService {
    ApiDataResponse<UserResponse> register(RegisterRequest req);
    ApiDataResponse<JWTResponse> login(LoginRequest req);
    ApiDataResponse<UserResponse> me(String username);
}
