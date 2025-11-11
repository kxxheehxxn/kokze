package org.ozea.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    public Map<String, Object> loginWithKakao(String code) {
        // TODO: 카카오 토큰 교환 넣기
        return Map.of(
                "token", "KAKAO_JWT",
                "code", code
        );
    }
}