package org.ozea.recommend.dto;

import lombok.Builder;
import lombok.Data;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.product.dto.ProductDto;

import java.util.List;

@Data
@Builder
public class RecommendResponseDto {
    private String type;
    private String resultCode;
    private Integer score;
    private List<ProductDto> products;

    public static RecommendResponseDto from(
            List<ProductDto> products,
            AssessmentResultDto result
    ) {
        return RecommendResponseDto.builder()
                .type(result.getType())
                .resultCode(result.getResultCode())
                .score(result.getScore())
                .products(products)
                .build();
    }
}