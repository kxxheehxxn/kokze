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
                        .id(1L)
                        .name("청년 적금 4%")
                        .build()
        );
    }
}