package edu.service.imp;

import lombok.RequiredArgsConstructor;
import edu.model.dto.request.EvaluationCriteriaRequest;
import edu.model.dto.response.EvaluationCriteriaResponse;
import edu.model.entity.EvaluationCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.repo.EvaluationCriteriaRepository;
import edu.service.EvaluationCriteriaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {
@Autowired
    private final EvaluationCriteriaRepository repository;

    @Override
    public List<EvaluationCriteriaResponse> getAllCriteria() {
        return repository.findAll()
                .stream()
                .map(EvaluationCriteriaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationCriteriaResponse getCriterionById(Long id) {
        EvaluationCriteria criterion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));
        return EvaluationCriteriaResponse.fromEntity(criterion);
    }

    @Override
    public EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request) {
        EvaluationCriteria criterion = request.toEntity();
        return EvaluationCriteriaResponse.fromEntity(repository.save(criterion));
    }

    @Override
    public EvaluationCriteriaResponse updateCriterion(Long id, EvaluationCriteriaRequest request) {
        EvaluationCriteria existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));
        existing.setCriterionName(request.getCriterionName());
        existing.setDescription(request.getDescription());
        existing.setMaxScore(request.getMaxScore());
        return EvaluationCriteriaResponse.fromEntity(repository.save(existing));
    }

    @Override
    public void deleteCriterion(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Criterion not found");
        }
        repository.deleteById(id);
    }
}
