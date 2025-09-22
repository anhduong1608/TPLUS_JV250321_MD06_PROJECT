package edu.model.dto.response;

import lombok.*;
import edu.model.entity.Student;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long studentId;
    private String studentCode;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;

    public static StudentResponse fromEntity(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentCode(student.getStudentCode())
                .major(student.getMajor())
                .className(student.getClassName())
                .dateOfBirth(student.getDateOfBirth())
                .address(student.getAddress())
                .build();
    }
}
