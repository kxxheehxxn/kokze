package org.ozea.ai.service;


import org.ozea.product.dto.ProductDto;
import org.ozea.user.domain.User;

import java.util.List;

/*
* Ai기반 추천을 위한 포트 인터페이스
* LLM, AI API 파인튜닝 모델 연결
* */
public interface AiRecommendationClient {
    /*
     * 주어진 유저의 후보 상품 리스트를받아 AI 추천 순서 리스트 반환
     * */
    List<ProductDto> rankProducts(User userOrNull, List<ProductDto> candidates);

}
