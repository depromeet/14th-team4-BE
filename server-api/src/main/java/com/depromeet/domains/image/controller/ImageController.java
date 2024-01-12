package com.depromeet.domains.image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.S3.S3UploaderService;
import com.depromeet.common.exception.CustomResponseEntity;

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
	public CustomResponseEntity<String> getPresignedUrl(@RequestParam("path") String path, @RequestParam("fileName") String fileName) {
		return CustomResponseEntity.success(s3UploaderService.getPreSignedUrl(path, fileName));
	}
}
