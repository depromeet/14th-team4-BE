package com.depromeet.domains.review.entity;

import java.time.LocalDate;

import com.depromeet.domains.common.entity.BaseTimeEntity;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE REVIEW SET REVIEW.DELETED_AT = CURRENT_TIMESTAMP WHERE REVIEW.REVIEW_ID = ?")
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
	private LocalDate visitedAt;

	private String imageUrl;

	@Column(nullable = false)
	private Integer visitTimes;

	private String description;
}
