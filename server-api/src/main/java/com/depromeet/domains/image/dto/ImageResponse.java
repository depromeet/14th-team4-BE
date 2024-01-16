package com.depromeet.domains.image.dto;

import lombok.Getter;

@Getter
public class ImageResponse {
	private String presignedUrl;

	public ImageResponse(String presignedUrl) {
		this.presignedUrl = presignedUrl;
	}
}
