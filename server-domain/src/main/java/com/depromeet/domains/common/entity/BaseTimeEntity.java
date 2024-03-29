package com.depromeet.domains.common.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

	@CreatedDate
	@Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
	@Comment("생성일자")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP")
	@Comment("수정일자")
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;
}
