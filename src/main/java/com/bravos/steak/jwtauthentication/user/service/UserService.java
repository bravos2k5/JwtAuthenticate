package com.bravos.steak.jwtauthentication.user.service;

import com.bravos.steak.jwtauthentication.user.model.dto.UserAuthCredentials;
import com.bravos.steak.jwtauthentication.user.model.dto.UserInfo;
import com.bravos.steak.jwtauthentication.user.model.entity.User;

public interface UserService {

    UserAuthCredentials getUserAuthCredentials(String username);

    UserInfo getUserInfoById(Long id);

    UserInfo createUser(User user);

    UserInfo getCurrentUserInfo();

}
