package org.ozea.assessment.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.repository.AssessmentRepository;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRiskProfileProvider implements RiskProfileProvider {

    private final AssessmentRepository assessmentRepository;

    @Override
    public AssessmentResultDto getLatestRiskProfile(User user) {
        return assessmentRepository
                .findTopByUserAndTypeOrderByCreatedAtDesc(user, "RISK_TOLERANCE")
                .orElseThrow(() -> new IllegalStateException("평가 결과가 없습니다."));
    }
}
