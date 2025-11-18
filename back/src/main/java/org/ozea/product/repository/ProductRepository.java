package org.ozea.product.repository;

import org.ozea.product.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductDto, Long> {
    List<ProductDto> findByRiskLevel(String riskLevel);

    List<ProductDto> findByCategory(String category);

    List<ProductDto> findByRiskLevelAndCategory(String riskLevel, String category);
}
