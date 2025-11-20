package org.ozea.assessment.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.repository.AssessmentRepository;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Transactional
    @Override
    public AssessmentResultDto evaluate(AssessmentAnswerDto dto, User user) {
        int score = calculateScore(dto);
        String resultCode = mapScoreToType(score);

        AssessmentResultDto result = AssessmentResultDto.builder()
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode(resultCode)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();

        return assessmentRepository.save(result);
    }

    private String mapScoreToType(int score) {
        if (score <= 25) return "CONSERVATIVE";
        if (score <= 50) return "BALANCED";
        if (score <= 75) return "AGGRESSIVE";
        return "VERY_AGGRESSIVE";
    }

    private int calculateScore(AssessmentAnswerDto dto) {
        return 0;
        //TODO: 가중치 통해서 구현해야함
    }

    @Override
    public AssessmentResultDto getLatestResultFor(User user) {
        return assessmentRepository
                .findTopByUserAndTypeOrderByCreatedAtDesc(user, "RISK_TOLERANCE")
                .orElseThrow(() -> new IllegalStateException("위험 성향 평가 결과가 없습니다."));
    }
}