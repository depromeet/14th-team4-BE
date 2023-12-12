package com.depromeet.user.entity;

import java.util.UUID;

import com.depromeet.user.enums.Role;
import com.depromeet.user.enums.SocialType;

import lombok.Builder;
import lombok.Getter;


@Getter
public class User {
	private String id;
	private String nickname;
	private String email;
	private Role role;
	private SocialType socialType;
	private String socialId;
	@Builder
	public User(String nickname, String email, Role role, SocialType socialType, String socialId) {
		this.id = UUID.randomUUID().toString();
		this.nickname = nickname;
		this.email = email;
		this.role = role;
		this.socialType = socialType;
		this.socialId = socialId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public SocialType getSocialType() {
		return socialType;
	}

	public void setSocialType(SocialType socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
}
