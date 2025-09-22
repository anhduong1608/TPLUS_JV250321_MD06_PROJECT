package edu.model.dto.response;

import lombok.*;
import edu.model.entity.EvaluationCriteria;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationCriteriaResponse {
    private Long criterionId;
    private String criterionName;
    private String description;
    private BigDecimal maxScore;

    public static EvaluationCriteriaResponse fromEntity(EvaluationCriteria entity) {
        return EvaluationCriteriaResponse.builder()
                .criterionId(entity.getCriterionId())
                .criterionName(entity.getCriterionName())
                .description(entity.getDescription())
                .maxScore(entity.getMaxScore())
                .build();
    }
}
