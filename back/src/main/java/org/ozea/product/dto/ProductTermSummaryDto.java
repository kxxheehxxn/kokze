package org.ozea.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTermSummaryDto {
    private Long productId;
    private String name;
    private String summary;
}
