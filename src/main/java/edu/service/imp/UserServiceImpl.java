package edu.service.imp;


import edu.model.dto.request.CreateUserRequest;
import edu.model.dto.request.UpdateRoleRequest;
import edu.model.dto.request.UpdateStatusRequest;
import edu.model.dto.request.UpdateUserRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import edu.model.entity.Role;
import edu.model.entity.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.repo.RoleRepository;
import edu.repo.UserRepository;
import edu.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiDataResponse<List<UserResponse>> getAllUsers(String roleFilter) {
        List<Users> all = userRepository.findAll();
        if (roleFilter != null && !roleFilter.isBlank()) {
            all = all.stream()
                    .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRoleName().equalsIgnoreCase(roleFilter)))
                    .collect(Collectors.toList());
        }
        List<UserResponse> data = all.stream().map(this::mapToUserResponse).collect(Collectors.toList());
        return new ApiDataResponse<>(true, "OK", data, null, HttpStatus.OK);
    }

    @Override
    public ApiDataResponse<UserResponse> getUserById(Long id) {
        Optional<Users> o = userRepository.findById(id);
        if (o.isEmpty()) return new ApiDataResponse<>(false, "Not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);
        return new ApiDataResponse<>(true, "OK", mapToUserResponse(o.get()), null, HttpStatus.OK);
    }

    @Override
    public ApiDataResponse<UserResponse> createUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return new ApiDataResponse<>(false, "Username exists", null, "USERNAME_EXISTS", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return new ApiDataResponse<>(false, "Email exists", null, "EMAIL_EXISTS", HttpStatus.BAD_REQUEST);
        }

        Users u = Users.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .isActive(true)
                .build();

        // roles
        if (req.getRoleNames() != null && !req.getRoleNames().isEmpty()) {
            for (String rn : req.getRoleNames()) {
                Role r = roleRepository.findByRoleName(rn)
                        .orElseGet(() -> roleRepository.save(new Role(null, rn)));
                u.getRoles().add(r);
            }
        } else {
            // default STUDENT
            Role r = roleRepository.findByRoleName("STUDENT")
                    .orElseGet(() -> roleRepository.save(new Role(null, "STUDENT")));
            u.getRoles().add(r);
        }

        userRepository.save(u);
        return new ApiDataResponse<>(true, "Created", mapToUserResponse(u), null, HttpStatus.CREATED);
    }

    @Override
    public ApiDataResponse<UserResponse> updateUser(Long id, UpdateUserRequest req) {
        Users u = userRepository.findById(id).orElse(null);
        if (u == null) return new ApiDataResponse<>(false, "Not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);

        if (req.getEmail() != null && !req.getEmail().equals(u.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            return new ApiDataResponse<>(false, "Email exists", null, "EMAIL_EXISTS", HttpStatus.BAD_REQUEST);
        }

        u.setFullName(req.getFullName() != null ? req.getFullName() : u.getFullName());
        u.setEmail(req.getEmail() != null ? req.getEmail() : u.getEmail());
        u.setPhoneNumber(req.getPhoneNumber() != null ? req.getPhoneNumber() : u.getPhoneNumber());

        userRepository.save(u);
        return new ApiDataResponse<>(true, "Updated", mapToUserResponse(u), null, HttpStatus.OK);
    }

    @Override
    public ApiDataResponse<UserResponse> updateStatus(Long id, UpdateStatusRequest req) {
        Users u = userRepository.findById(id).orElse(null);
        if (u == null) return new ApiDataResponse<>(false, "Not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);
        u.setIsActive(req.getIsActive());
        userRepository.save(u);
        return new ApiDataResponse<>(true, "Status updated", mapToUserResponse(u), null, HttpStatus.OK);
    }

    @Override
    public ApiDataResponse<UserResponse> updateRoles(Long id, UpdateRoleRequest req, String currentUsername) {
        Users target = userRepository.findById(id).orElse(null);
        if (target == null) return new ApiDataResponse<>(false, "Not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);

        // load current user
        Users current = userRepository.findByUsername(currentUsername).orElse(null);

        boolean targetHasAdmin = target.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getRoleName()));
        boolean currentIsAdmin = current != null && current.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getRoleName()));

        // rule: ADMIN không được thay đổi quyền của ADMIN khác
        if (targetHasAdmin && current != null && !Objects.equals(current.getUserId(), target.getUserId())) {
            return new ApiDataResponse<>(false, "Cannot change roles of another ADMIN", null, "FORBIDDEN", HttpStatus.FORBIDDEN);
        }

        // set new roles
        List<Role> newRoles = new ArrayList<>();
        for (String rn : req.getRoleNames()) {
            Role r = roleRepository.findByRoleName(rn.toUpperCase())
                    .orElseGet(() -> roleRepository.save(new Role(null, rn.toUpperCase())));
            newRoles.add(r);
        }
        target.setRoles(newRoles);
        userRepository.save(target);

        return new ApiDataResponse<>(true, "Roles updated", mapToUserResponse(target), null, HttpStatus.OK);
    }

    @Override
    public ApiDataResponse<Void> deleteUser(Long id) {
        Users u = userRepository.findById(id).orElse(null);
        if (u == null) return new ApiDataResponse<>(false, "Not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);
        userRepository.deleteById(id);
        return new ApiDataResponse<>(true, "Deleted", null, null, HttpStatus.OK);
    }

    @Override
    public UserResponse mapToUserResponse(Users u) {
        // convert roles to authorities (SimpleGrantedAuthority)
        Collection<SimpleGrantedAuthority> auths = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
                .collect(Collectors.toList());

        return UserResponse.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .isActive(u.getIsActive())
                .authorities(auths)
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
