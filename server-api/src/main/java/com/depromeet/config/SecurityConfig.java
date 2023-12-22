package com.depromeet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.depromeet.auth.jwt.JwtAuthenticationFilter;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.oauth2.handler.CustomOAuth2SuccessHandler;
import com.depromeet.auth.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private static final String[] PATTERNS = {
		"/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**",
		"/jwt-test", "/auth/refresh", "/auth/logout"
	};
	private final JwtService jwtTokenProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) //csrf 비활성
			.formLogin(AbstractHttpConfigurer::disable) //폼 로그인 비활성
			.httpBasic(AbstractHttpConfigurer::disable) //HTTP 기본인증 비활성
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(request -> request
				.requestMatchers(PATTERNS).permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2Login ->
				oauth2Login
					.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
					.successHandler(customOAuth2SuccessHandler)
			)
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
