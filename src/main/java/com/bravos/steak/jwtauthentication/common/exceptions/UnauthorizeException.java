package com.bravos.steak.jwtauthentication.common.exceptions;

public class UnauthorizeException extends RuntimeException {
    public UnauthorizeException(String message) {
        super(message);
    }
}
