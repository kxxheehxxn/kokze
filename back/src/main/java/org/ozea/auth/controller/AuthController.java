package org.ozea.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ozea.auth.dto.LoginRequest;
import org.ozea.auth.dto.LoginResponse;
import org.ozea.auth.dto.MeResponse;
import org.ozea.auth.dto.SignupRequest;
import org.ozea.auth.service.LocalAuthService;
import org.ozea.auth.service.KakaoAuthService;
import org.ozea.common.dto.ApiResponse;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LocalAuthService localAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        log.info("POST /api/auth/login called: {}", req);
        LoginResponse res = localAuthService.login(req);
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<Map<String, Object>>> kakao(@Valid @RequestBody Map<String, String> body) {
        Map<String, Object> res = kakaoAuthService.loginWithKakao(body.get("code"));
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            MeResponse me = MeResponse.builder()
                    .authenticated(false)
                    .build();
            return ResponseEntity.ok(ApiResponse.ok(me));
        }
        String email = (String) auth.getPrincipal();
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            MeResponse me = MeResponse.builder()
                    .authenticated(false)
                    .build();
            return ResponseEntity.ok(ApiResponse.ok(me));
        }
        MeResponse me = MeResponse.builder()
                .authenticated(true)
                .role(user.getRole())
                .tendency(user.getTendency())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(me));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<LoginResponse>> signup(
            @Valid @RequestBody SignupRequest req
    ){
        LoginResponse res = localAuthService.signup(req);
        return ResponseEntity.ok(ApiResponse.ok(res));
    }
}