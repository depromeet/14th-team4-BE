package com.depromeet.entity.user;

import com.depromeet.common.BaseTimeEntity;
import com.depromeet.user.enums.Role;
import com.depromeet.user.enums.SocialType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType type;

	private String profileImageUrl;

	@Column(nullable = false)
	private String nickName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role userRole;

	@Column(nullable = false)
	private String socialId;
}
