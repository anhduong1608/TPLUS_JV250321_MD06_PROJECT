package edu.repo;

import edu.model.entity.InternshipAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {
    List<InternshipAssignment> findByStudent_StudentId(Long studentId);
    List<InternshipAssignment> findByMentor_MentorId(Long mentorId);
    List<InternshipAssignment> findByInternshipPhase_PhaseId(Long phaseId);
}
