// model/dto/request/UpdateStatusRequest.java
package model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusRequest {
    private Boolean isActive;
}
