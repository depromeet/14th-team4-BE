package com.depromeet.domains.feed.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Where(clause = "deletedAt is NULL")
@SQLDelete(sql = "UPDATE Feed SET deletedAt = CURRENT_TIMESTAMP WHERE feedId = ?")

public class Feed extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedId;

	@Column(nullable = false)
	private Long storeId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Integer rating;

	@Column(length = 5000, nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Long likeCnt;

	@Column(nullable = false)
	private Long commentCnt;

}
