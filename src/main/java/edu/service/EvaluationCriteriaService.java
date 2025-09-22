package edu.service;

import edu.model.dto.request.EvaluationCriteriaRequest;
import edu.model.dto.response.EvaluationCriteriaResponse;

import java.util.List;

public interface EvaluationCriteriaService {
    List<EvaluationCriteriaResponse> getAllCriteria();
    EvaluationCriteriaResponse getCriterionById(Long id);
    EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request);
    EvaluationCriteriaResponse updateCriterion(Long id, EvaluationCriteriaRequest request);
    void deleteCriterion(Long id);
}
