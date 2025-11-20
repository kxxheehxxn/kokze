package org.ozea.product.service;

import lombok.RequiredArgsConstructor;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbProductCatalog implements ProductCatalog {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> findByRiskLevel(String riskLevel) {
        return productRepository.findByRiskLevel(riskLevel);
    }

    @Override
    public List<ProductDto> findPopular() {

        //TODO: 인기순으로 하거나 별도 필드에서 필터링 예정
        //우선 대중적인 상품 추천
        return productRepository.findByRiskLevel("MEDIUM");
    }
}
