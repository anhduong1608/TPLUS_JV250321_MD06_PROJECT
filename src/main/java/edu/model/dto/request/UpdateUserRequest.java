// model/dto/request/UpdateUserRequest.java
package edu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "Họ tên người dùng không được để trống")
    private String fullName;

    @NotBlank(message = "Email người dùng không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại người dùng không được để trống")
    private String phone;

    private LocalDateTime updatedAt;
}
