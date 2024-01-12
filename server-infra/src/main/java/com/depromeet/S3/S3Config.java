package com.depromeet.S3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Value("${cloud.aws.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region}")
	private String region;

	@Value("${cloud.aws.endpoint}")
	private String endPoint;

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		return AmazonS3ClientBuilder
			.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.build();
	}
}
