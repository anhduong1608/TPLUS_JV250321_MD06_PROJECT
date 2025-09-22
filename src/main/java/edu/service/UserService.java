// service/UserService.java
package edu.service;

import edu.model.dto.request.CreateUserRequest;
import edu.model.dto.request.UpdateRoleRequest;
import edu.model.dto.request.UpdateStatusRequest;
import edu.model.dto.request.UpdateUserRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.UserResponse;
import edu.model.entity.Users;

import java.util.List;

public interface UserService {
    ApiDataResponse<List<UserResponse>> getAllUsers(String roleFilter);
    ApiDataResponse<UserResponse> getUserById(Long id);
    ApiDataResponse<UserResponse> createUser(CreateUserRequest req);
    ApiDataResponse<UserResponse> updateUser(Long id, UpdateUserRequest req);
    ApiDataResponse<UserResponse> updateStatus(Long id, UpdateStatusRequest req);
    ApiDataResponse<UserResponse> updateRoles(Long id, UpdateRoleRequest req, String currentUsername);
    ApiDataResponse<Void> deleteUser(Long id);
    UserResponse mapToUserResponse(Users u);
}
