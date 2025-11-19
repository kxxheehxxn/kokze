package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.repository.AssessmentRepository;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.repository.ProductRepository;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleBasedRecommender implements Recommender {

    private final AssessmentRepository assessmentRepository;
    private final ProductRepository productRepository;

    @Override
    public RecommendResponseDto recommendFor(User user) {
        //비로그인시
        if(user == null){
            //우선적으로 미디움에 해당하는 상품 추천
            List<ProductDto> products = productRepository.findByRiskLevel("MEDIUM");
            return RecommendResponseDto.guest(products);
        }

        //로그인시
        AssessmentResultDto result = assessmentRepository
                .findTopByUserAndTypeOrderByCreatedAtDesc(user, "RISK_TOLERANCE")
                .orElseThrow(() -> new IllegalStateException("위험 성향 평가 결과가 없습니다."));

        String riskLevel = mapRisk(result.getResultCode());

        List<ProductDto> products = productRepository.findByRiskLevel(riskLevel);

        return RecommendResponseDto.from(products, result);
    }

    private String mapRisk(String resultCode) {
        return switch (resultCode) {
            case "CONSERVATIVE" -> "LOW";
            case "AGGRESSIVE" -> "HIGH";
            default -> "MEDIUM";
        };
    }
}
