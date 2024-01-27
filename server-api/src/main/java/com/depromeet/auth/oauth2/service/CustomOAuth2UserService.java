package com.depromeet.auth.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.depromeet.auth.oauth2.CustomOAuth2User;
import com.depromeet.auth.oauth2.OAuth2Attribute;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.enums.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final String KAKAO = "kakao";
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		/**
		 * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
		 * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
		 * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
		 */
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = getUserNameAttributeName(userRequest);
		// Map<String, Object> attributes = oAuth2User.getAttributes();
		Map<String, Object> attributes;

		if (socialType.name().contains("apple")) {
			String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
			attributes = decodeJwtTokenPayload(idToken);
			attributes.put("id_token", idToken);
		} else {
			attributes = oAuth2User.getAttributes();
		}

		OAuth2Attribute extractAttributes = OAuth2Attribute.of(socialType, userNameAttributeName, attributes);
		// 기존 가입된 유저인지 확인
		User createdUser = getUser(extractAttributes, socialType);

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(createdUser.getUserRole().getDescription())),
			attributes, extractAttributes.getNameAttributeKey(), createdUser.getUserId(), createdUser.getUserRole());
	}

	private String getUserNameAttributeName(final OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
	}

	private SocialType getSocialType(String registrationId) {

		if (KAKAO.equals(registrationId)) {
			return SocialType.KAKAO;
		}
		return SocialType.APPLE;
	}

	/**
	 * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
	 * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveUser()를 호출하여 회원을 저장한다.
	 */
	private User getUser(OAuth2Attribute attributes, SocialType socialType) {
		User findUser = userRepository.findBySocialTypeAndSocialId(socialType, attributes.getSocialId()).orElse(null);

		if (findUser == null) {
			return saveUser(attributes, socialType);
		}
		return findUser;
	}

	/**
	 * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
	 * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
	 */
	private User saveUser(OAuth2Attribute attributes, SocialType socialType) {
		User createdUser = attributes.toEntity(socialType);
		return userRepository.save(createdUser);
	}

	//JWT Payload부분 decode 메서드
	public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
		Map<String, Object> jwtClaims = new HashMap<>();
		try {
			String[] parts = jwtToken.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();

			byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
			String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> map = mapper.readValue(decodedString, Map.class);
			jwtClaims.putAll(map);

		} catch (JsonProcessingException e) {
			log.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
		}
		return jwtClaims;
	}
}
