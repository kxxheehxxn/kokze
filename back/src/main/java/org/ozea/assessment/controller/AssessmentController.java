package org.ozea.assessment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.service.AssessmentService;
import org.ozea.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessment")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<AssessmentResultDto>> submit(
            @Valid @RequestBody AssessmentAnswerDto dto
    ) {
        AssessmentResultDto result = assessmentService.evaluate(dto);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/me")
    public AssessmentResultDto myResult() {
        // TODO: JWT에서 사용자 꺼내서 조회
        return new AssessmentResultDto();
    }
}