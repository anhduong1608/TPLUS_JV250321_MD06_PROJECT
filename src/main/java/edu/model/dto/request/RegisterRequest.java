package edu.model.dto.request;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Tên người dùng không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải tối thiểu 8 ký tự")
    private String password;

    @NotBlank(message = "Họ tên người dùng không được để trống")
    private String fullName;

    @NotBlank(message = "Email người dùng không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại người dùng không được để trống")
    private String phone;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private Set<String> roles;
}
