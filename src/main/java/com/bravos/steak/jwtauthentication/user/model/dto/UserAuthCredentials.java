package com.bravos.steak.jwtauthentication.user.model.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos.steak.jwtauthentication.user.model.entity.User}
 */
@Value
public class UserAuthCredentials implements Serializable {
    Long id;
    String username;
    String password;
}