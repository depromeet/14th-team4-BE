package com.depromeet.domains.review.entity;

import java.time.LocalDateTime;

import com.depromeet.domains.common.entity.BaseTimeEntity;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private Integer rating;

	@Column(nullable = false)
	private LocalDateTime visitedAt;

	private String imageUrl;

	@Column(nullable = false)
	private Integer visitTimes;

	private String description;
}
