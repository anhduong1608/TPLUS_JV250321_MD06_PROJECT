// model/dto/request/UpdateUserRequest.java
package model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
}
