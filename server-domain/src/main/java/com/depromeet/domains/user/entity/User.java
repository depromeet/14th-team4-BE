package com.depromeet.domains.user.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;
import com.depromeet.enums.Role;
import com.depromeet.enums.SocialType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType socialType;

	private String profileImageUrl;

	@Column(nullable = false)
	private String nickName;

	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role userRole;

	@Column(nullable = false)
	private String socialId;

	@Builder
	public User(SocialType socialType, String profileImageUrl, String nickName, String email, Role userRole,
		String socialId) {
		this.socialType = socialType;
		this.profileImageUrl = profileImageUrl;
		this.nickName = nickName;
		this.email = email;
		this.userRole = userRole;
		this.socialId = socialId;
	}

	public void updateNickname(String nickName) {
		this.nickName = nickName;
	}
}
