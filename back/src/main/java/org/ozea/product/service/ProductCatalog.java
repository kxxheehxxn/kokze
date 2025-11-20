package org.ozea.product.service;

import org.ozea.product.dto.ProductDto;

import java.util.List;

public interface ProductCatalog {
    List<ProductDto> findByRiskLevel(String riskLevel); // 위험 성향 조회

    List<ProductDto> findPopular(); //비로그인 조회
}
