package com.depromeet.entity.heart;

import com.depromeet.common.BaseTimeEntity;

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
public class Heart extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long heartId;

	@Column(name = "image", columnDefinition = "varchar(500) null comment '이미지'")
	private Long userId;

	@Column(nullable = false)
	private Long ddoeatlogId;
}
