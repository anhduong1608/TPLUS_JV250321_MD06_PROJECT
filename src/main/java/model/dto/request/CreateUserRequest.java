// model/dto/request/CreateUserRequest.java  (ADMIN tạo user)
package model.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;

    private List<String> roleNames;
}
