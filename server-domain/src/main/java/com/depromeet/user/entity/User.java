package com.depromeet.user.entity;

import java.util.UUID;

import com.depromeet.user.enums.Role;
import com.depromeet.user.enums.SocialType;

import lombok.Builder;
import lombok.Getter;


@Getter
public class User {
	private final String id;
	private final String nickname;
	private final String email;
	private final Role role;
	private final SocialType socialType;
	private final String socialId;
	@Builder
	public User(String nickname, String email, Role role, SocialType socialType, String socialId) {
		this.id = UUID.randomUUID().toString();
		this.nickname = nickname;
		this.email = email;
		this.role = role;
		this.socialType = socialType;
		this.socialId = socialId;
	}
}
