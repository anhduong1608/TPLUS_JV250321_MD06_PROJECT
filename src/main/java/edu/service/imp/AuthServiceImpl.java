package edu.service.imp;

import edu.model.dto.request.LoginRequest;
import edu.model.dto.request.RegisterRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.JWTResponse;
import edu.model.dto.response.UserResponse;
import edu.model.entity.Role;
import edu.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.repo.RoleRepository;
import edu.repo.UserRepository;
import edu.security.jwt.JWTProvider;
import edu.service.AuthService;
import edu.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
   @Autowired
   RoleRepository roleRepository;
   @Autowired
   PasswordEncoder passwordEncoder;
   @Autowired
   AuthenticationManager authenticationManager;
   @Autowired
    JWTProvider  jwtProvider;
   @Autowired
   UserService userService;

    @Override
    public ApiDataResponse<UserResponse> register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return new ApiDataResponse<>(false, "Username exists", null, "USERNAME_EXISTS", HttpStatus.BAD_REQUEST);
        }
        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            return new ApiDataResponse<>(false, "Username không được để trống", null, "USERNAME_EMPTY", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return new ApiDataResponse<>(false, "Email exists", null, "EMAIL_EXISTS", HttpStatus.BAD_REQUEST);
        }
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            return new ApiDataResponse<>(false, "Pass không được để trống", null, "PASS_EMPTY", HttpStatus.BAD_REQUEST);
        }

        if (req.getPassword().length() < 4) {return new ApiDataResponse<>(false, "Password errors", null, "PASSWORD_TOO_LONG", HttpStatus.BAD_REQUEST);}
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            return new ApiDataResponse<>(false, "Email không được để trống", null, "EMAIL_EMPTY", HttpStatus.BAD_REQUEST);
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!req.getEmail().matches(emailRegex)) {
            return new ApiDataResponse<>(false, "Email không đúng định dạng", null, "EMAIL_INVALID", HttpStatus.BAD_REQUEST);
        }
        // ensure STUDENT role exists
        Role studentRole = roleRepository.findByRoleName("STUDENT")
                .orElseGet(() -> roleRepository.save(new Role(null, "STUDENT")));

        Users u = Users.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .roles(new ArrayList<>())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .isActive(true)
                .build();
        u.getRoles().add(studentRole);
        userRepository.save(u);

        UserResponse ur = userService.mapToUserResponse(u);
        return new ApiDataResponse<>(true, "Register success", ur, null, HttpStatus.CREATED);
    }

    @Override
    public ApiDataResponse<JWTResponse> login(LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );

            // build token
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = jwtProvider.generateJWT(auth.getName());

            // load user info
            Users u = userRepository.findByUsername(auth.getName()).orElseThrow();

            JWTResponse resp = JWTResponse.builder()
                    .username(u.getUsername())
                    .fullName(u.getFullName())
                    .email(u.getEmail())
                    .phoneNumber(u.getPhoneNumber())
                    .status(u.getIsActive())
                    .authorities(auth.getAuthorities())
                    .typeAuthentication("Bearer")
                    .jwt(jwt)
                    .build();

            return new ApiDataResponse<>(true, "Login success", resp, null, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return new ApiDataResponse<>(false, "Invalid credentials", null, "BAD_CREDENTIALS", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ApiDataResponse<>(false, "Auth error", null, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiDataResponse<UserResponse> me(String username) {
        Users u = userRepository.findByUsername(username).orElse(null);
        if (u == null) return new ApiDataResponse<>(false, "User not found", null, "NOT_FOUND", HttpStatus.NOT_FOUND);
        UserResponse ur = userService.mapToUserResponse(u);
        return new ApiDataResponse<>(true, "OK", ur, null, HttpStatus.OK);
    }
}

