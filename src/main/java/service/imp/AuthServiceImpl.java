package service.imp;

import model.dto.request.LoginRequest;
import model.dto.request.RegisterRequest;
import model.dto.response.ApiDataResponse;
import model.dto.response.JWTResponse;
import model.dto.response.UserResponse;
import model.entity.Role;
import model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repo.RoleRepository;
import repo.UserRepository;
import security.jwt.JWTProvider;
import service.AuthService;
import service.UserService;

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
        if (userRepository.existsByEmail(req.getEmail())) {
            return new ApiDataResponse<>(false, "Email exists", null, "EMAIL_EXISTS", HttpStatus.BAD_REQUEST);
        }

        // ensure STUDENT role exists
        Role studentRole = roleRepository.findByRoleName("STUDENT")
                .orElseGet(() -> roleRepository.save(new Role(null, "STUDENT")));

        Users u = Users.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
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

