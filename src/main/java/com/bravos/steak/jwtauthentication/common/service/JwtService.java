package com.bravos.steak.jwtauthentication.common.service;

import com.bravos.steak.jwtauthentication.common.model.TokenClaims;

public interface JwtService {

    String generateToken(Long id);

    TokenClaims parseToken(String token);

    boolean isTokenValid(String token);

    TokenClaims verifyTokenAndParse(String token);

}
