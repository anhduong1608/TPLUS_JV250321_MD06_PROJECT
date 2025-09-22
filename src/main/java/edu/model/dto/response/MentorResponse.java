package edu.model.dto.response;

import lombok.*;
import edu.model.entity.Mentor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MentorResponse {
    private Long mentorId;
    private String username;
    private String fullName;
    private String email;
    private String department;
    private String academicRank;

    public static MentorResponse fromEntity(Mentor mentor) {
        return MentorResponse.builder()
                .mentorId(mentor.getMentorId())
                .username(mentor.getUser().getUsername())
                .fullName(mentor.getUser().getFullName())
                .email(mentor.getUser().getEmail())
                .department(mentor.getDepartment())
                .academicRank(mentor.getAcademicRank())
                .build();
    }
}
