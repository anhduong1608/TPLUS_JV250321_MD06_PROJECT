package edu.model.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
    private Collection<? extends GrantedAuthority> authorities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}