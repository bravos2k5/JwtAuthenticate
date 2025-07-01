package com.bravos.steak.jwtauthentication.user.service.impl;

import com.bravos.steak.jwtauthentication.common.exceptions.BadRequestException;
import com.bravos.steak.jwtauthentication.common.exceptions.ConflictDataException;
import com.bravos.steak.jwtauthentication.user.model.dto.UserAuthCredentials;
import com.bravos.steak.jwtauthentication.user.model.dto.UserInfo;
import com.bravos.steak.jwtauthentication.user.model.entity.User;
import com.bravos.steak.jwtauthentication.user.repo.UserRepository;
import com.bravos.steak.jwtauthentication.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAuthCredentials getUserAuthCredentials(String username) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }

        return userRepository.getUserAuthCredentialsByUsername(username);

    }

    @Override
    public UserInfo getUserInfoById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ID must not be null or less than or equal to zero");
        }

        return userRepository.getUserInfoById(id);

    }

    @Override
    public UserInfo createUser(User user) {
        if (user == null) {
            throw new BadRequestException("User must not be null");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("Username must not be null or empty");
        }

        if(userRepository.existsByIdAndUsername(user.getId(), user.getUsername())) {
            throw new ConflictDataException("User with this ID and username already exists");
        }

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }

        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getDateOfBirth()
        );
    }

    @Override
    public UserInfo getCurrentUserInfo() {
        return getUserInfoById((Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
