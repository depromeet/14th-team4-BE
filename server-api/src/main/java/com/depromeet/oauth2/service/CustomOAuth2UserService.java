package com.depromeet.oauth2.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.depromeet.oauth2.dto.CustomOAuth2User;
import com.depromeet.oauth2.dto.OAuth2Attribute;
import com.depromeet.user.entity.User;
import com.depromeet.user.entity.UserRepository;
import com.depromeet.user.enums.SocialType;

import lombok.RequiredArgsConstructor;

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
		Map<String, Object> attributes = oAuth2User.getAttributes();
		OAuth2Attribute extractAttributes = OAuth2Attribute.of(socialType, userNameAttributeName, attributes);

		User createdUser = getUser(extractAttributes, socialType);

		return new CustomOAuth2User(Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
			attributes, extractAttributes.getNameAttributeKey(), createdUser.getId());
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
		User findUser = userRepository.findBySocialTypeAndEmail(socialType, attributes.getEmail()).orElse(null);

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
}
