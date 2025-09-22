package edu.service;

import edu.model.entity.InternshipPhase;
import java.util.List;

public interface InternshipPhaseService {
    InternshipPhase createPhase(InternshipPhase phase);
    InternshipPhase updatePhase(Long id, InternshipPhase phase);
    void deletePhase(Long id);
    List<InternshipPhase> getAllPhases();
    InternshipPhase getPhaseById(Long id);
}
