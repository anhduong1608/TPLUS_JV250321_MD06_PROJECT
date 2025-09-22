// controller/UserController.java
package edu.controller;

import edu.model.dto.request.CreateUserRequest;
import edu.model.dto.request.UpdateRoleRequest;
import edu.model.dto.request.UpdateStatusRequest;
import edu.model.dto.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edu.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 4 GET /api/users (ADMIN) — optional role filter e.g. ?role=MENTOR
    @GetMapping
    public ResponseEntity<ApiDataResponse<List<UserResponse>>> getAll(@RequestParam(value = "role", required = false) String role) {
        ApiDataResponse<List<UserResponse>> resp = userService.getAllUsers(role);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 5 GET /api/users/{user_id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> getOne(@PathVariable("id") Long id) {
        ApiDataResponse<UserResponse> resp = userService.getUserById(id);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 6 POST /api/users (ADMIN create user)
    @PostMapping
    public ResponseEntity<ApiDataResponse<UserResponse>> create(@RequestBody CreateUserRequest req) {
        ApiDataResponse<UserResponse> resp = userService.createUser(req);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 7 PUT /api/users/{user_id} update basic
    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> update(@PathVariable("id") Long id,
                                                                @RequestBody UpdateUserRequest req) {
        ApiDataResponse<UserResponse> resp = userService.updateUser(id, req);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 8 PUT /api/users/{user_id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateStatus(@PathVariable("id") Long id,
                                                                      @RequestBody UpdateStatusRequest req) {
        ApiDataResponse<UserResponse> resp = userService.updateStatus(id, req);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 9 PUT /api/users/{user_id}/role
    @PutMapping("/{id}/role")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateRole(@PathVariable("id") Long id,
                                                                    @RequestBody UpdateRoleRequest req) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        ApiDataResponse<UserResponse> resp = userService.updateRoles(id, req, currentUsername);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }

    // 10 DELETE /api/users/{user_id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Void>> delete(@PathVariable("id") Long id) {
        ApiDataResponse<Void> resp = userService.deleteUser(id);
        return ResponseEntity.status(resp.getStatus()).body(resp);
    }
}
