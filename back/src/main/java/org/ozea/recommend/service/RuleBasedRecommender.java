package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.service.RiskProfileProvider;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.service.ProductCatalog;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ruleBasedRecommender")
@RequiredArgsConstructor
public class RuleBasedRecommender implements Recommender {

    private final RiskProfileProvider riskProfileProvider;
    private final ProductCatalog productCatalog;

    @Override
    public RecommendResponseDto recommendFor(User user) {
        //비로그인시
        if(user == null){
            //우선적으로 미디움에 해당하는 상품 추천
            List<ProductDto> products = productCatalog.findPopular();
            return RecommendResponseDto.guest(products);
        }

        //로그인시
        AssessmentResultDto result = riskProfileProvider.getLatestRiskProfile(user);

        String riskLevel = mapRisk(result.getResultCode());

        List<ProductDto> products = productCatalog.findByRiskLevel(riskLevel);

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
