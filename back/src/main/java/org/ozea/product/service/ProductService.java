package org.ozea.product.service;

import lombok.RequiredArgsConstructor;
import org.ozea.ai.service.TermSummarizer;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.dto.ProductTermSummaryDto;
import org.ozea.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final TermSummarizer termSummarizer;

    public List<ProductDto> findAll() {
        return productRepository.findAll();
    }

    public ProductTermSummaryDto getTermSummary(Long productId) {
        ProductDto product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));

        String rawTerms = product.getDescription();

        String summary = termSummarizer.summarizeTo3Lines(rawTerms);

        return ProductTermSummaryDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .summary(summary)
                .build();
    }
}