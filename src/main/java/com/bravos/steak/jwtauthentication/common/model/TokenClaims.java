package com.bravos.steak.jwtauthentication.common.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenClaims {

    @NonNull
    private Long id;

    @NonNull
    private Long iat;

    @NonNull
    private Long exp;

    @NonNull
    private Long jti;

    private Map<String,Object> otherClaims;

    public Map<String, Object> toMap() {
        return Map.of(
                "id",id,
                "iat",iat,
                "exp",exp,
                "jti",jti,
                "otherClaims",otherClaims
        );
    }

}
