package com.depromeet.domains.user.entity;

import org.hibernate.annotations.SQLDelete;

import com.depromeet.domains.common.entity.BaseTimeEntity;
import com.depromeet.enums.Role;
import com.depromeet.enums.SocialType;
import com.depromeet.enums.UserLevel;

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
//@Where(clause = "deletedAt is NULL")
@SQLDelete(sql = "UPDATE User SET deletedAt = CURRENT_TIMESTAMP WHERE userId = ?")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType socialType;

	@Column(nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role userRole;

	@Column(nullable = false)
	private String socialId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserLevel level = UserLevel.LEVEL1;

	@Column(nullable = false)
	private Integer myFeedCnt = 0;

	private String profileImageUrl;

	@Builder
	public User(SocialType socialType, String nickname, Role userRole, String socialId) {
		this.socialType = socialType;
		this.nickname = nickname;
		this.userRole = userRole;
		this.socialId = socialId;
	}

	public void updateUserRole() {
		this.userRole = Role.USER;
	}

	public void updateNickname(String nickName) {
		this.nickname = nickName;
	}

	public void updateProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void updateUserLevel(UserLevel level) {
		this.level = level;
	}

	public void increaseMyFeedCount() {
		this.myFeedCnt++;
	}

	public void decreaseMyFeedCount() {
		this.myFeedCnt = Math.max(0, this.myFeedCnt - 1);
	}
}
