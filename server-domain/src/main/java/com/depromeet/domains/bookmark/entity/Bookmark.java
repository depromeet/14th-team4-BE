package com.depromeet.domains.bookmark.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE bookmark SET deleted_at = CURRENT_TIMESTAMP WHERE bookmark_id = ?")
public class Bookmark extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookmarkId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
}
