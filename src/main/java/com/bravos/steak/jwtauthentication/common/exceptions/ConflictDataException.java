package com.bravos.steak.jwtauthentication.common.exceptions;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(String message) {
        super(message);
    }
}
