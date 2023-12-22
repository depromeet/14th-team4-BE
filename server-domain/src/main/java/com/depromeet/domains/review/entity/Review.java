package com.depromeet.domains.review.entity;

import java.time.LocalDateTime;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(nullable = false)
	private Long storeId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Float rating;

	@Column(nullable = false)
	private LocalDateTime visitedAt;

	private String imageUrl;

	@Column(nullable = false)
	private Integer visitTimes;

	private String description;
}
