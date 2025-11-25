package org.ozea.ai.service;

/*
* 더미 구현
* 현재는 이자율이 높은 순서로 정렬
* 이후 LLM 연동시 이 클래스 교체 및 새로운 구현체를 만들어 @Primary 로 관리
* */

import org.ozea.product.dto.ProductDto;
import org.ozea.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DummyAiRecommendationClient implements AiRecommendationClient {

    @Override
    public List<ProductDto> rankProducts(User userOrNull, List<ProductDto> candidates) {
        //현재는 user를 쓰지 않지만 나중에 유저 정보를 포함해 프롬프트 구성을 위함
        return candidates.stream()
                .sorted(Comparator.comparing(
                        ProductDto::getInterestRate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
    }
}
