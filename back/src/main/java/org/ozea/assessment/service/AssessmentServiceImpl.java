package org.ozea.assessment.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    @Override
    public AssessmentResultDto evaluate(AssessmentAnswerDto dto) {
        // TODO: 점수 계산해서 4타입 중 하나 리턴
        return new AssessmentResultDto("TYPE_A");
    }
}