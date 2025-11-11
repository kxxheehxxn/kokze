package org.ozea.product.service;

import lombok.RequiredArgsConstructor;
import org.ozea.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    public List<ProductDto> findAll() {
        // TODO: 크롤링 결과 or DB
        return List.of(
                ProductDto.builder()
                        .id("p1")
                        .name("청년 적금 4%")
                        .bank("OO은행")
                        .description("청년 대상 우대금리")
                        .summary("청년 우대 적금\n금리 4%\n1년 만기")
                        .category("SAVING")
                        .build()
        );
    }
}