package com.bravos.steak.jwtauthentication.user.model.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.bravos.steak.jwtauthentication.user.model.entity.User}
 */
@Value
public class UserInfo implements Serializable {
    Long id;
    String username;
    String fullName;
    String email;
    LocalDate dateOfBirth;
}