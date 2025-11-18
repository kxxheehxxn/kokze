package org.ozea.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponse {
    private boolean authenticated;
    private String email;
    private String name;
    private String role;
    private String tendency;
}
