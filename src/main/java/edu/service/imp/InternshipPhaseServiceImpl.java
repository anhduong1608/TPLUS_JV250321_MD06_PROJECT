package edu.service.imp;

import lombok.RequiredArgsConstructor;
import edu.model.entity.InternshipPhase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.repo.InternshipPhaseRepository;
import edu.service.InternshipPhaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {
    @Autowired
    InternshipPhaseRepository repository;


    @Override
    public InternshipPhase createPhase(InternshipPhase phase) {
        return repository.save(phase);
    }

    @Override
    public InternshipPhase updatePhase(Long id, InternshipPhase phase) {
        InternshipPhase existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found"));
        existing.setPhaseName(phase.getPhaseName());
        existing.setStartDate(phase.getStartDate());
        existing.setEndDate(phase.getEndDate());
        existing.setDescription(phase.getDescription());
        return repository.save(existing);
    }

    @Override
    public void deletePhase(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<InternshipPhase> getAllPhases() {
        return repository.findAll();
    }

    @Override
    public InternshipPhase getPhaseById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
