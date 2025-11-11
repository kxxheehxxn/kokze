package org.ozea.assessment.controller;

import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.service.AssessmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping("/submit")
    public AssessmentResultDto submit(@RequestBody AssessmentAnswerDto dto) {
        return assessmentService.evaluate(dto);
    }

    @GetMapping("/me")
    public AssessmentResultDto myResult() {
        // TODO: JWT에서 사용자 꺼내서 조회
        return new AssessmentResultDto("UNKNOWN");
    }
}