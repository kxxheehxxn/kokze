package org.ozea.assessment.service;

import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;

public interface AssessmentService {
    AssessmentResultDto evaluate(AssessmentAnswerDto dto);
}