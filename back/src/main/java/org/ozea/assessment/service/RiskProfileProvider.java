package org.ozea.assessment.service;

import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.user.domain.User;

public interface RiskProfileProvider {
    AssessmentResultDto getLatestRiskProfile(User user);
}
