package org.ozea.recommend.service;

import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;

public interface Recommender {
    RecommendResponseDto recommendFor(User user);
}
