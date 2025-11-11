package org.ozea.auth.controller;

import org.ozea.auth.dto.LoginRequest;
import org.ozea.auth.dto.LoginResponse;
import org.ozea.auth.service.KakaoAuthService;
import org.ozea.auth.service.LocalAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LocalAuthService localAuthService;
    private final KakaoAuthService kakaoAuthService;

    public AuthController(LocalAuthService localAuthService, KakaoAuthService kakaoAuthService) {
        this.localAuthService = localAuthService;
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(localAuthService.login(req));
    }

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, Object>> kakao(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(kakaoAuthService.loginWithKakao(body.get("code")));
    }
}