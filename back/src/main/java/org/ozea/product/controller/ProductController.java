package org.ozea.product.controller;

import lombok.RequiredArgsConstructor;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> list() {
        return productService.findAll();
    }
}