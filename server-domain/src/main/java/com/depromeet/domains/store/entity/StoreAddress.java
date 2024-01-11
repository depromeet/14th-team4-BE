package com.depromeet.domains.store.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreAddress {

	private String jibunAddress;
	private String roadAddress;
	private String addressDetail;
}
