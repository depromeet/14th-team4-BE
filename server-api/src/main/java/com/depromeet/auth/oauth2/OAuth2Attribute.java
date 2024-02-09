package com.depromeet.auth.oauth2;

import java.util.Map;

import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.Role;
import com.depromeet.enums.SocialType;

import lombok.Builder;
import lombok.Getter;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuth2Attribute {
	private final Map<String, Object> attributes;
	private final String nameAttributeKey;
	private final String socialId;
	private final String nickname;
	private final String email;

	@Builder
	public OAuth2Attribute(Map<String, Object> attributes, String nameAttributeKey, String socialId, String nickname,
		String email) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.socialId = socialId;
		this.nickname = nickname;
		this.email = email;
	}

	/**
	 * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
	 * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
	 */
	public static OAuth2Attribute of(SocialType socialType, String userNameAttributeName,
		Map<String, Object> attributes) {

		if (socialType == SocialType.KAKAO) {
			return ofKakao(userNameAttributeName, attributes);
		}
		return ofKakao(userNameAttributeName, attributes);
	}

	private static OAuth2Attribute ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

		return OAuth2Attribute.builder()
			.socialId(String.valueOf(attributes.get("id")))
			.attributes(kakaoAccount)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public User toEntity(SocialType socialType) {
		return User.builder()
			.socialType(socialType)
			.socialId(socialId)
			.nickName("게스트" + socialId)
			.userRole(Role.GUEST)
			.build();
	}
}
