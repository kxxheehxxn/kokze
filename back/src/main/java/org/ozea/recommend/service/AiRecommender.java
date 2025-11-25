package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.ai.service.AiRecommendationClient;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.service.RiskProfileProvider;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.service.ProductCatalog;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("aiRecommender")
@RequiredArgsConstructor
public class AiRecommender implements Recommender {

    private final RiskProfileProvider riskProfileProvider;
    private final ProductCatalog productCatalog;
    private final AiRecommendationClient aiRecommendationClient;

    @Override
    public RecommendResponseDto recommendFor(User user) {
        List<ProductDto> candidates;
        AssessmentResultDto result = null;

        if (user == null) {
            candidates = productCatalog.findPopular();
        } else {
            result = riskProfileProvider.getLatestRiskProfile(user);
            String riskLevel = mapRisk(result.getResultCode());
            candidates = productCatalog.findByRiskLevel(riskLevel);
        }

        List<ProductDto> ranked = aiRecommendationClient.rankProducts(user, candidates);

        if(user == null){
            return RecommendResponseDto.guest(ranked);
        } else {
            return RecommendResponseDto.from(ranked, result);
        }
    }

    private String mapRisk(String resultCode) {
        return switch (resultCode) {
            case "CONSERVATIVE" -> "LOW";
            case "AGGRESSIVE" -> "HIGH";
            default -> "MEDIUM";
        };
    }
}
