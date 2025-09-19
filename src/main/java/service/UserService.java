// service/UserService.java
package service;

import model.dto.request.CreateUserRequest;
import model.dto.request.UpdateRoleRequest;
import model.dto.request.UpdateStatusRequest;
import model.dto.request.UpdateUserRequest;
import model.dto.response.ApiDataResponse;
import model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    ApiDataResponse<List<UserResponse>> getAllUsers(String roleFilter);
    ApiDataResponse<UserResponse> getUserById(Long id);
    ApiDataResponse<UserResponse> createUser(CreateUserRequest req);
    ApiDataResponse<UserResponse> updateUser(Long id, UpdateUserRequest req);
    ApiDataResponse<UserResponse> updateStatus(Long id, UpdateStatusRequest req);
    ApiDataResponse<UserResponse> updateRoles(Long id, UpdateRoleRequest req, String currentUsername);
    ApiDataResponse<Void> deleteUser(Long id);
    UserResponse mapToUserResponse(model.entity.Users u);
}
