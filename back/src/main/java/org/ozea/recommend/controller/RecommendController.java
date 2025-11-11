package org.ozea.recommend.controller;

import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.recommend.service.RecommendService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping
    public RecommendResponseDto recommend() {
        return recommendService.recommendForCurrentUser();
    }
}