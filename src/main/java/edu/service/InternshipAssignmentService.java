package edu.service;

import edu.model.entity.InternshipAssignment;
import java.util.List;

public interface InternshipAssignmentService {
    InternshipAssignment assignStudent(InternshipAssignment assignment);
    InternshipAssignment updateAssignment(Long id, InternshipAssignment assignment);
    void deleteAssignment(Long id);
    List<InternshipAssignment> getAssignmentsByStudent(Long studentId);
    List<InternshipAssignment> getAssignmentsByMentor(Long mentorId);
    List<InternshipAssignment> getAssignmentsByPhase(Long phaseId);
}
