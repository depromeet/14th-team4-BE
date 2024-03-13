package com.depromeet.domains.user.repository;

import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	@Query("SELECT u FROM User u WHERE u.socialType = :socialType AND u.socialId = :socialId AND u.deletedAt is null")
	Optional<User> findBySocialTypeAndSocialId(@Param("socialType") SocialType socialType, @Param("socialId") String socialId);

}
