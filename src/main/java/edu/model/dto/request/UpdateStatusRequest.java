// model/dto/request/UpdateStatusRequest.java
package edu.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusRequest {
    private Boolean isActive;
}
