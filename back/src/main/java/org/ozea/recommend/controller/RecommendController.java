package org.ozea.recommend.controller;

import lombok.RequiredArgsConstructor;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.recommend.service.RecommendService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    // 기존: rule 기반 기본 추천
    // GET /api/recommend
    // GET /api/recommend?strategy=rule
    // GET /api/recommend?strategy=ai
    @GetMapping
    public RecommendResponseDto recommend(
            @RequestParam(name = "strategy", defaultValue = "rule") String strategy
    ) {
        return recommendService.recommend(strategy);
    }
}