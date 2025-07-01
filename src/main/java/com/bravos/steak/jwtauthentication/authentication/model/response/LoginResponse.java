package com.bravos.steak.jwtauthentication.authentication.model.response;

import com.bravos.steak.jwtauthentication.user.model.dto.UserInfo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    String jwt;

    UserInfo userInfo;

}
