package com.depromeet.domains.follow.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import com.depromeet.domains.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long followId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id", referencedColumnName = "user_id")
	private User following; //일반유저

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followed_id", referencedColumnName = "user_id")
	private User followed; //크리에이터
}
