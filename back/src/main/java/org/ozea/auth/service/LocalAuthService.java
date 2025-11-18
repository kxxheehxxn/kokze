package org.ozea.auth.service;

import lombok.RequiredArgsConstructor;
import org.ozea.auth.dto.LoginRequest;
import org.ozea.auth.dto.LoginResponse;
import org.ozea.auth.dto.SignupRequest;
import org.ozea.auth.jwt.JwtProvider;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocalAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest req){
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);


        if(user == null){
            return LoginResponse.fail("이메일을 찾을 수 없습니다.");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            return LoginResponse.fail("비밀번호가 옳지 않습니다.");
        }
        if (Boolean.FALSE.equals(user.getActive())) {
            return LoginResponse.fail("유저가 비활성화 되어있습니다.");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getEmail());

        return LoginResponse.ok(token, user.getEmail());
    }

    public LoginResponse signup(SignupRequest req) {
        if(userRepository.existsByEmail(req.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String passwordHash = passwordEncoder.encode(req.getPassword());

        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(passwordHash)
                .name(req.getName())
                .phone(req.getPhone())
                .role("USER")
                .provider("LOCAL")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getEmail());
        return LoginResponse.ok(token, user.getEmail());
    }
}