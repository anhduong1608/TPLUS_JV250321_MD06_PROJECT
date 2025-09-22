package edu.model.dto.request;

import lombok.*;
import edu.model.entity.Mentor;
import edu.model.entity.Users;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MentorRequest {
    private Long userId; // liên kết với Users
    private String department;
    private String academicRank;

    public Mentor toEntity(Users user) {
        return Mentor.builder()
                .mentorId(user.getUserId())
                .user(user)
                .department(department)
                .academicRank(academicRank)
                .build();
    }
}
