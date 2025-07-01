package com.bravos.steak.jwtauthentication.authentication.service;

import com.bravos.steak.jwtauthentication.authentication.model.request.LoginRequest;
import com.bravos.steak.jwtauthentication.authentication.model.request.RegisterRequest;
import com.bravos.steak.jwtauthentication.authentication.model.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse register(RegisterRequest registerRequest);

}
