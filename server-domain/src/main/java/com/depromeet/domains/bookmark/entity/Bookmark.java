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
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE BOOKMARK SET BOOKMARK.DELETED_AT = CURRENT_TIMESTAMP WHERE BOOKMARK.BOOKMARK_ID = ?")
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
