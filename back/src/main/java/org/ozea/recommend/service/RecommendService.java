package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.repository.AssessmentRepository;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.repository.ProductRepository;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final ProductRepository productRepository;

    public RecommendResponseDto recommendForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다"));

        AssessmentResultDto result = assessmentRepository
                .findTopByUserAndTypeOrderByCreatedAtDesc(user, "RISK_TOLERANCE")
                .orElseThrow(() -> new IllegalStateException("위험 성향 평가 결과가 없습니다."));

        String riskLevel = mapRisk(result.getResultCode());

        List<ProductDto> products = productRepository.findByRiskLevel(riskLevel);

        return RecommendResponseDto.from(products, result);

    }

    private String mapRisk(String resultCode) {
        return switch(resultCode){
            case "CONSERVATIVE" -> "LOW";
            case "AGGRESSIVE" -> "HIGH";
            default -> "MEDIUM";
        };
    }
}