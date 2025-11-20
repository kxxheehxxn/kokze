package org.ozea.assessment.service;

import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.user.domain.User;

public interface AssessmentService {
    AssessmentResultDto evaluate(AssessmentAnswerDto dto, User user);

    AssessmentResultDto getLatestResultFor(User user);
}