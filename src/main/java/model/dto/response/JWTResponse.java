// model/dto/response/JWTResponse.java
package model.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JWTResponse {
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean status;
    private Collection<? extends GrantedAuthority> authorities;
    private String typeAuthentication; // e.g. "Bearer"
    private String jwt;
}
