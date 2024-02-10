package com.depromeet.auth.apple;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppleFeignClientConfiguration {

	@Bean
	public AppleFeignClientErrorDecoder appleFeignClientErrorDecoder() {
		return new AppleFeignClientErrorDecoder(new ObjectMapper());
	}
}
