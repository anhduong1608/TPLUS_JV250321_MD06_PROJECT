package edu.service.imp;

import lombok.RequiredArgsConstructor;
import edu.model.entity.InternshipAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.repo.InternshipAssignmentRepository;
import edu.service.InternshipAssignmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {
    @Autowired
    private final InternshipAssignmentRepository repository;

    @Override
    public InternshipAssignment assignStudent(InternshipAssignment assignment) {
        return repository.save(assignment);
    }

    @Override
    public InternshipAssignment updateAssignment(Long id, InternshipAssignment assignment) {
        InternshipAssignment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        existing.setMentor(assignment.getMentor());
        existing.setInternshipPhase(assignment.getInternshipPhase());
        existing.setStatus(assignment.getStatus());
        return repository.save(existing);
    }

    @Override
    public void deleteAssignment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<InternshipAssignment> getAssignmentsByStudent(Long studentId) {
        return repository.findByStudent_StudentId(studentId);
    }

    @Override
    public List<InternshipAssignment> getAssignmentsByMentor(Long mentorId) {
        return repository.findByMentor_MentorId(mentorId);
    }

    @Override
    public List<InternshipAssignment> getAssignmentsByPhase(Long phaseId) {
        return repository.findByInternshipPhase_PhaseId(phaseId);
    }
}
