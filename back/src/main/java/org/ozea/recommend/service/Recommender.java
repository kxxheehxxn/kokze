package org.ozea.recommend.service;

import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;

public interface Recommender {
    /**
 * Generate a recommendation response tailored to the specified user.
 *
 * @param user the target user for whom recommendations are produced
 * @return a RecommendResponseDto containing recommended items and related metadata
 */
RecommendResponseDto recommendFor(User user);
}