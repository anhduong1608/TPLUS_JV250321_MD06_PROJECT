package edu.model.dto.request;

import lombok.*;
import edu.model.entity.Student;
import edu.model.entity.Users;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentRequest {
    private String studentCode;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
    private Long userId; // ID của user được liên kết

    // Chuyển từ DTO sang Entity
    public Student toEntity(Users user) {
        return Student.builder()
                .studentCode(this.studentCode)
                .major(this.major)
                .className(this.className)
                .dateOfBirth(this.dateOfBirth)
                .address(this.address)
                .user(user) // liên kết với User đã tồn tại
                .build();
    }
}
