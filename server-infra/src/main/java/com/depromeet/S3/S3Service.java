package com.depromeet.S3;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import com.amazonaws.SdkClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
	@Value("${cloud.aws.bucket}")
	private String bucketName;

	private final AmazonS3 amazonS3;

	public String getPreSignedUrl(String fileName) {
		String saveName = fileName + "_" + UUID.randomUUID();
		GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(saveName);
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucketName, fileName)
				.withMethod(HttpMethod.PUT)
				.withExpiration(getPreSignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 30;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	public void deleteFile(String fileName) {
		try {
			amazonS3.deleteObject(bucketName, fileName);
			log.info("[S3 File Delete] {}", fileName);
		} catch (SdkClientException e) {
			log.error("[S3 File Delete 실패]", e);
			throw new RuntimeException("[S3 File Delete 실패]", e);
		}
	}
}
