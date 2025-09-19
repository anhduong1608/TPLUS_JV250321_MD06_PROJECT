// model/dto/request/UpdateRoleRequest.java
package model.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
    private List<String> roleNames;
}
