package com.depromeet.domains.image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.S3.S3UploaderService;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.image.dto.ImageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/images")
public class ImageController {
	private final S3UploaderService s3UploaderService;

	/**
	 * Presigned-url 발급
	 */
	@GetMapping("/presigned-url")
	public CustomResponseEntity<ImageResponse> getPresignedUrl(@RequestParam("fileName") String fileName) {
		return CustomResponseEntity.success(new ImageResponse(s3UploaderService.getPreSignedUrl(fileName)));
	}
}
