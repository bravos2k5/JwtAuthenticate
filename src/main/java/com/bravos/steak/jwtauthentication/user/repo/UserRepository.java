package com.bravos.steak.jwtauthentication.user.repo;

import com.bravos.steak.jwtauthentication.user.model.dto.UserAuthCredentials;
import com.bravos.steak.jwtauthentication.user.model.dto.UserInfo;
import com.bravos.steak.jwtauthentication.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.bravos.steak.jwtauthentication.user.model.dto.UserAuthCredentials(u.id, u.username, u.password) " +
           "FROM User u WHERE u.username = :username")
    UserAuthCredentials getUserAuthCredentialsByUsername(@Param("username") String username);

    @Query("SELECT new com.bravos.steak.jwtauthentication.user.model.dto.UserInfo(u.id, u.username, u.fullName, u.email, u.dateOfBirth) " +
           "FROM User u WHERE u.id = :id")
    UserInfo getUserInfoById(@Param("id") Long id);

    boolean existsByIdAndUsername(Long id, String username);
}