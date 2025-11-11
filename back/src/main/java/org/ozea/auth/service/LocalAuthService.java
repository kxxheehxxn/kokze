package org.ozea.auth.service;

import lombok.RequiredArgsConstructor;
import org.ozea.auth.dto.LoginRequest;
import org.ozea.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthService {

    public LoginResponse login(LoginRequest req) {
        // TODO: 진짜 유저 검증으로 교체
        if ("test@ozea.org".equals(req.getEmail()) && "1234".equals(req.getPassword())) {
            return LoginResponse.ok("DUMMY_JWT", req.getEmail());
        }
        return LoginResponse.fail("INVALID_CREDENTIALS");
    }
}