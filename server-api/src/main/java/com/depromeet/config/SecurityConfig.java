package com.depromeet.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.depromeet.auth.jwt.JwtAuthenticationFilter;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.oauth2.handler.CustomOAuth2FailureHandler;
import com.depromeet.auth.oauth2.handler.CustomOAuth2SuccessHandler;
import com.depromeet.auth.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private static final String[] PATTERNS = {
		"/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**",
			"/docs/index.html", "/api/v1/docs/**", "/jwt-test", "/auth/refresh", "/auth/logout", "/api/v1/auth/token/reissue",
	};
	private final JwtService jwtTokenProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer(){
		return web -> web.ignoring()
				.requestMatchers(String.valueOf(PathRequest
						.toStaticResources()
						.atCommonLocations())
				);
	}

//	@Override
//	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/api/v1/docs/**")
//				.addResourceLocations("classpath:/static/docs/");
//	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) //csrf 비활성
			.cors(AbstractHttpConfigurer::disable)
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
					.authorizationEndpoint(
						authEndpoint -> authEndpoint.baseUri("/oauth2/authorization/**")) // 이 url로 접근시 로그인을 요청한다
					.redirectionEndpoint(redirectEndpoint -> redirectEndpoint.baseUri("/login/oauth2/code/**"))
					.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
					.successHandler(customOAuth2SuccessHandler)
					.failureHandler(customOAuth2FailureHandler)
			)
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
