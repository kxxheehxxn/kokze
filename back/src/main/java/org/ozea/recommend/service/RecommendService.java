package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final UserRepository userRepository;

    @Qualifier("ruleBasedRecommender")
    private final Recommender ruleBasedRecommender;

    @Qualifier("aiRecommender")
    private final Recommender aiRecommender;

    public RecommendResponseDto recommend(String strategy) {

        User userOrNull = getCurrentUserOrNull();

        String mode = strategy == null ? "rule" : strategy.toLowerCase();

        Recommender target = switch (mode) {
            case "ai" -> aiRecommender;
            case "rule" -> ruleBasedRecommender;
            default -> ruleBasedRecommender;
        };

        return target.recommendFor(userOrNull);
    }

    private User getCurrentUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        String email = (String) auth.getPrincipal();

        return userRepository.findByEmail(email).orElse(null);
    }
}