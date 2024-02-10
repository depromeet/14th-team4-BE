package com.depromeet.auth.apple;

import java.io.IOException;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper;

	/**
	 * 애플 소셜 로그인 Feign API 연동 시 발생되는 오류에 대해서 예외 처리를 수행.
	 *
	 * @param methodKey Feign Client 메서드 이름
	 * @param response  응답 정보
	 * @return 예외를 리턴한다
	 */
	@Override
	public Exception decode(String methodKey, Response response) {
		Object body = null;
		if (response != null && response.body() != null) {
			try {
				body = objectMapper.readValue(response.body().toString(), Object.class);
			} catch (IOException e) {
				log.error("Error decoding response body", e);
			}
		}

		log.error("애플 소셜 로그인 Feign API Feign Client 호출 중 오류가 발생되었습니다. body: {}", body);

		// return new MyServerException(400, "애플 소셜 로그인 Feign API Feign Client 호출 오류");
		return new CustomException(Result.BAD_REQUEST);
	}
}
