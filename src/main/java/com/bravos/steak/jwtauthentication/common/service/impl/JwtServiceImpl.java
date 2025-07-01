package com.bravos.steak.jwtauthentication.common.service.impl;

import com.bravos.steak.jwtauthentication.common.model.TokenClaims;
import com.bravos.steak.jwtauthentication.common.service.JwtService;
import com.bravos.steak.jwtauthentication.common.service.SnowflakeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final String header = "{" +
            "\"alg\":\"" + "HS256" + "\"," +
            "\"typ\":\"JWT\"" +
            "}";

    private final String base64Header = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
    private final ObjectMapper objectMapper;
    private final PrivateKey jwtPrivateKey;
    private final PublicKey jwtPublicKey;
    private final SnowflakeService snowflakeService;

    public JwtServiceImpl(ObjectMapper objectMapper, PrivateKey jwtPrivateKey,
                          PublicKey jwtPublicKey, SnowflakeService snowflakeService) {
        this.objectMapper = objectMapper;
        this.jwtPrivateKey = jwtPrivateKey;
        this.jwtPublicKey = jwtPublicKey;
        this.snowflakeService = snowflakeService;
    }

    @Override
    public String generateToken(Long id) {

        long now = System.currentTimeMillis();
        StringBuilder tokenBuilder = new StringBuilder(base64Header);
        TokenClaims claims = TokenClaims.builder()
                .id(id)
                .iat(now)
                .exp(now + 3600000)
                .jti(snowflakeService.generateId())
                .build();

        String base64Claims;
        try {
            base64Claims = base64UrlEncode(objectMapper.writeValueAsBytes(claims));
        } catch (JsonProcessingException e) {
            log.error("Error serializing claims to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Error when generating token");
        }

        tokenBuilder.append(".");
        tokenBuilder.append(base64Claims);

        String signature = signature(tokenBuilder.toString());
        tokenBuilder.append(".").append(signature);

        return tokenBuilder.toString();

    }

    @Override
    public TokenClaims parseToken(String token) {
        String[] parts = token.split("\\.");
        try {
            return objectMapper.readValue(
                    Base64.getUrlDecoder().decode(parts[1]),
                    TokenClaims.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing token claims: {}", e.getMessage(), e);
            throw new RuntimeException("Error when parsing token");
        } catch (IOException e) {
            log.error("Error decoding token claims: {}", e.getMessage(), e);
            throw new RuntimeException("Error when decoding token claims");
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        String[] parts = token.split("\\.");
        if(parts.length != 3 || !parts[0].equals(base64Header) || parts[1].isBlank() || parts[2].isBlank()) {
            return false;
        }
        return verifySignature(parts[0] + "." + parts[1], parts[2]);
    }

    @Override
    public TokenClaims verifyTokenAndParse(String token) {
        if (token != null && !token.isBlank() && isTokenValid(token)) {
            TokenClaims tokenClaims = parseToken(token);
            long now = System.currentTimeMillis();
            if (tokenClaims.getExp() > now && tokenClaims.getIat() <= now) {
                return tokenClaims;
            }
            throw new IllegalArgumentException("Token is expired or not yet valid");
        }
        throw new IllegalArgumentException("Token is invalid");
    }

    private String base64UrlEncode(byte[] inputBytes) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(inputBytes);
    }

    private String signature(String data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(jwtPrivateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signDataBytes = signature.sign();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signDataBytes);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            log.error("Error when get signature data: {}",e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean verifySignature(String data, String signatureValue) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            byte[] signedDataBytes = Base64.getUrlDecoder().decode(signatureValue);
            signature.initVerify(jwtPublicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(signedDataBytes);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            log.warn("Failed when verify data: {}", e.getMessage(), e);
            return false;
        }
    }

}
