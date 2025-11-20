package org.ozea.product.service;

import lombok.RequiredArgsConstructor;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> findAll() {
        return productRepository.findAll();
    }
}