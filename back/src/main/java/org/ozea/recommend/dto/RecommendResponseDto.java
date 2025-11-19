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

    /**
     * Creates a RecommendResponseDto from an assessment result and a list of products.
     *
     * @param products the products to include in the response
     * @param result   the assessment result providing the response's type, resultCode, and score
     * @return a RecommendResponseDto whose type, resultCode, and score are taken from the given AssessmentResultDto and whose products list is the provided products
     */
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

    /**
     * Create a guest RecommendResponseDto populated with default metadata.
     *
     * @param products the list of products to include in the response
     * @return a RecommendResponseDto with type set to "GUEST", resultCode set to "DEFAULT", score set to null, and the provided products
     */
    public static RecommendResponseDto guest(List<ProductDto> products){
        return RecommendResponseDto.builder()
                .type("GUEST")
                .resultCode("DEFAULT")
                .score(null)
                .products(products)
                .build();
    }
}