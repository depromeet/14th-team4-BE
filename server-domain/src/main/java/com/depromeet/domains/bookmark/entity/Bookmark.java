package com.depromeet.domains.bookmark.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Where(clause = "deletedAt is NULL")
@SQLDelete(sql = "UPDATE Bookmark SET deletedAt = CURRENT_TIMESTAMP WHERE bookmarkId = ?")

public class Bookmark extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookmarkId;

	private Long userId;

	private Long storeId;
}
