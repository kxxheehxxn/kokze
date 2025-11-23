package org.ozea.product.service;

import lombok.RequiredArgsConstructor;
import org.ozea.crawling.service.CrawlingService;
import org.ozea.product.dto.ProductDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Primary // -> 기본 구현체로 사용하기 위함
@RequiredArgsConstructor
public class CrawlingProductCatalog implements ProductCatalog {

    private final CrawlingService crawlingService;

    private final Map<String, List<ProductDto>> riskCache = new ConcurrentHashMap<>();
    private volatile List<ProductDto> popularCache;

    private Instant lastRefresh = Instant.EPOCH;
    private static final Duration TTL = Duration.ofMinutes(10);

    @Override
    public List<ProductDto> findByRiskLevel(String riskLevel) {
        refreshCacheIfNeeded();
        return riskCache.getOrDefault(riskLevel, List.of());
    }

    private synchronized void refreshCacheIfNeeded() {
        if (Instant.now().isBefore(lastRefresh.plus(TTL))) {
            return;
        }

        List<ProductDto> crawled = crawlingService.fetchProducts();

        riskCache.clear();

        crawled.forEach(p->{
            String risk = p.getRiskLevel();
            if(risk == null){
                return;
            }
            riskCache.computeIfAbsent(risk, k -> new ArrayList<>()).add(p);
        });

        List<ProductDto> medium = riskCache.get("MEDIUM");
        if (medium != null && !medium.isEmpty()) {
            popularCache = medium;
        } else {
            popularCache = crawled.stream().limit(5).toList();
        }

        lastRefresh = Instant.now();
    }

    @Override
    public List<ProductDto> findPopular() {
        refreshCacheIfNeeded();
        return popularCache != null ? popularCache : List.of();
    }
}
