package edu.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Mentor {

    @Id
    @Column(name = "mentor_id")
    private Long mentorId; // trùng với UserID

    @OneToOne
    @MapsId
    @JoinColumn(name = "mentor_id")
    private Users user;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "academic_rank", length = 50)
    private String academicRank;

    @Column(name = "created_at", updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
