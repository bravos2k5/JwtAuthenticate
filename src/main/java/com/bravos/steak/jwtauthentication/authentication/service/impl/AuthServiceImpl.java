package com.bravos.steak.jwtauthentication.authentication.service.impl;

import com.bravos.steak.jwtauthentication.authentication.model.request.LoginRequest;
import com.bravos.steak.jwtauthentication.authentication.model.request.RegisterRequest;
import com.bravos.steak.jwtauthentication.authentication.model.response.LoginResponse;
import com.bravos.steak.jwtauthentication.authentication.service.AuthService;
import com.bravos.steak.jwtauthentication.common.exceptions.UnauthorizeException;
import com.bravos.steak.jwtauthentication.common.service.JwtService;
import com.bravos.steak.jwtauthentication.common.service.SnowflakeService;
import com.bravos.steak.jwtauthentication.user.model.dto.UserAuthCredentials;
import com.bravos.steak.jwtauthentication.user.model.dto.UserInfo;
import com.bravos.steak.jwtauthentication.user.model.entity.User;
import com.bravos.steak.jwtauthentication.user.repo.UserRepository;
import com.bravos.steak.jwtauthentication.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SnowflakeService snowflakeService;
    private final UserService userService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           JwtService jwtService, SnowflakeService snowflakeService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.snowflakeService = snowflakeService;
        this.userService = userService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserAuthCredentials authCredentials = userRepository.getUserAuthCredentialsByUsername(loginRequest.getUsername());
        if(authCredentials == null || !passwordEncoder.matches(loginRequest.getPassword(), authCredentials.getPassword())) {
            throw new UnauthorizeException("Invalid username or password");
        }
        String jwt = jwtService.generateToken(authCredentials.getId());
        UserInfo userInfo;
        try {
            userInfo = userRepository.getUserInfoById(authCredentials.getId());
        } catch (Exception e) {
            log.error("Failed to retrieve user information: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user information");
        }
        return new LoginResponse(jwt,userInfo);
    }

    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .id(snowflakeService.generateId())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .build();

        UserInfo userInfo = userService.createUser(user);
        String jwt = jwtService.generateToken(user.getId());

        return new LoginResponse(jwt, userInfo);
    }

}
