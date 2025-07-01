package com.bravos.steak.jwtauthentication.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
public class JwtConfiguration {

    @Bean
    public PrivateKey jwtPrivateKey() {
        String privateKey = readPemFile("private.pem");
        return convertPrivateKey(privateKey);
    }

    @Bean
    public PublicKey jwtPublicKey() {
        String publicKey = readPemFile("public.pem");
        return convertPublicKey(publicKey);
    }

    private String readPemFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey convertPrivateKey(String privateKey) {
        try {
            privateKey = privateKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\n", "")
                    .replaceAll(" ","");
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid private key");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Invalid algorithm");
        }
    }

    private PublicKey convertPublicKey(String publicKey) {
        try {
            publicKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\n", "")
                    .replaceAll(" ","");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid public key");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Invalid algorithm");
        }
    }

}
