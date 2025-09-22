package edu.controller;

import lombok.RequiredArgsConstructor;
import edu.model.dto.request.EvaluationCriteriaRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.EvaluationCriteriaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.service.EvaluationCriteriaService;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService service;

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<EvaluationCriteriaResponse>>> getAllCriteria() {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Lấy danh sách tiêu chí thành công",
                        service.getAllCriteria(), null, null)
        );
    }

    @GetMapping("/{criterion_id}")
    public ResponseEntity<ApiDataResponse<EvaluationCriteriaResponse>> getCriterionById(
            @PathVariable Long criterion_id) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Lấy chi tiết tiêu chí thành công",
                        service.getCriterionById(criterion_id), null, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<EvaluationCriteriaResponse>> createCriterion(
            @RequestBody EvaluationCriteriaRequest request) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Tạo tiêu chí thành công",
                        service.createCriterion(request), null, null)
        );
    }

    @PutMapping("/{criterion_id}")
    public ResponseEntity<ApiDataResponse<EvaluationCriteriaResponse>> updateCriterion(
            @PathVariable Long criterion_id,
            @RequestBody EvaluationCriteriaRequest request) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Cập nhật tiêu chí thành công",
                        service.updateCriterion(criterion_id, request), null, null)
        );
    }

    @DeleteMapping("/{criterion_id}")
    public ResponseEntity<ApiDataResponse<Void>> deleteCriterion(@PathVariable Long criterion_id) {
        service.deleteCriterion(criterion_id);
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Xóa tiêu chí thành công",
                        null, null, null)
        );
    }
}
