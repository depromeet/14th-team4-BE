package com.depromeet.domains.user.repository;

import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

	boolean existsByNickName(String nickname);
}
