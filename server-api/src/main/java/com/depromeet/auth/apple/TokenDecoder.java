package com.depromeet.auth.apple;

import java.util.Base64;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenDecoder {

	public static <T> T decodePayload(String token, Class<T> targetClass) {

		String[] tokenParts = token.split("\\.");
		String payloadJWT = tokenParts[1];
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(payloadJWT));
		ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return objectMapper.readValue(payload, targetClass);
		} catch (Exception e) {
			throw new RuntimeException("Error decoding token payload", e);
		}
	}
}
