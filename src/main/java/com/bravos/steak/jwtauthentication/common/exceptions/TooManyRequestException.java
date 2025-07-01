package com.bravos.steak.jwtauthentication.common.exceptions;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException(String message) {
        super(message);
    }
}
