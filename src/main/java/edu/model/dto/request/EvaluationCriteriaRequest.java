package edu.model.dto.request;

import lombok.*;
import edu.model.entity.EvaluationCriteria;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationCriteriaRequest {
    private String criterionName;
    private String description;
    private BigDecimal maxScore;

    public EvaluationCriteria toEntity() {
        return EvaluationCriteria.builder()
                .criterionName(this.criterionName)
                .description(this.description)
                .maxScore(this.maxScore)
                .build();
    }
}
