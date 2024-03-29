package com.depromeet.auth.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.depromeet.enums.Role;

import lombok.Getter;

// TODO: 삭제 예정
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
	private final Long userId;
	private final Role userRole;

	/**
	 * Constructs a {@code DefaultOAuth2User} using the provided parameters.
	 *
	 * @param authorities      the authorities granted to the user
	 * @param attributes       the attributes about the user
	 * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
	 *                         {@link #getAttributes()}
	 */
	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes, String nameAttributeKey, Long userId, Role userRole) {
		super(authorities, attributes, nameAttributeKey);
		this.userId = userId;
		this.userRole = userRole;
	}
}
