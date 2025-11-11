package org.ozea.recommend.service;

import org.ozea.recommend.dto.RecommendResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {

    public RecommendResponseDto recommendForCurrentUser() {
        // TODO: 성향 + 상품 + LLM 결과로 추천
        return new RecommendResponseDto();
    }
}