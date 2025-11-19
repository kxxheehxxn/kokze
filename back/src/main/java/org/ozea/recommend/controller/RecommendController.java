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

    @GetMapping
    public RecommendResponseDto recommend() {
        return recommendService.recommend();
    }
}