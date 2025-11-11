package org.ozea.crawling.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CrawlingService {

    public List<Map<String, Object>> fetchFinanceProducts() {
        // TODO: Jsoup 등으로 크롤링
        return List.of();
    }
}