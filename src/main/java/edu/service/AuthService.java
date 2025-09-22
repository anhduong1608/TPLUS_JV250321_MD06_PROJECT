// service/AuthService.java
package edu.service;

import edu.model.dto.request.LoginRequest;
import edu.model.dto.request.RegisterRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.JWTResponse;
import edu.model.dto.response.UserResponse;

public interface AuthService {
    ApiDataResponse<UserResponse> register(RegisterRequest req);
    ApiDataResponse<JWTResponse> login(LoginRequest req);
    ApiDataResponse<UserResponse> me(String username);
}
