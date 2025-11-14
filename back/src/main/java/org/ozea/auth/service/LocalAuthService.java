package org.ozea.auth.service;

import lombok.RequiredArgsConstructor;
import org.ozea.auth.dto.LoginRequest;
import org.ozea.auth.dto.LoginResponse;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest req){
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);


        if(user == null){
            return LoginResponse.fail("이메일을 찾을 수 없습니다.");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            return LoginResponse.fail("비밀번호가 옳지 않습니다");
        }
        // TODO JWT 발급 후 진행해야함
        return LoginResponse.ok("Token", user.getEmail());
    }
}