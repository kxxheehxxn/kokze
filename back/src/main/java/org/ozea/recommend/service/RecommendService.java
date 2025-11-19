package org.ozea.recommend.service;

import lombok.RequiredArgsConstructor;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final UserRepository userRepository;
    private final Recommender recommender;

    /**
     * Produce recommendation results for the current authenticated user, or for an anonymous session when no user is authenticated.
     *
     * @return a RecommendResponseDto containing recommendations for the current user, or recommendations for an anonymous session when no user is authenticated
     * @throws IllegalStateException if an authenticated principal exists but no corresponding User is found
     */
    public RecommendResponseDto recommend() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return recommender.recommendFor(null);
        }

        String email = (String) auth.getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다"));

        return recommender.recommendFor(user);
    }
}