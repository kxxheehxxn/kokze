package org.ozea.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private boolean success;
    private String token;
    private String message;
    private String email;

    public static LoginResponse ok(String token, String email) {
        return LoginResponse.builder()
                .success(true)
                .token(token)
                .email(email)
                .message("OK")
                .build();
    }

    public static LoginResponse fail(String msg) {
        return LoginResponse.builder()
                .success(false)
                .message(msg)
                .build();
    }
}