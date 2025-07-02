package org.example.expert.domain.user.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import org.example.expert.domain.user.dto.response.UserTestDto;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "false"))
    List<User> findUsersByNickname(String nickname);

    @Query("SELECT u.id, u.email, u.nickname FROM User u WHERE u.nickname = :nickname")
    List<UserTestDto> findUsers(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "false"))
    Optional<User> findByNicknameNoCache(@Param("nickname") String nickname);
}
