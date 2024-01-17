package com.depromeet.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.depromeet.auth.jwt.JwtAuthenticationFilter;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.oauth2.handler.CustomAuthenticationRequestFilter;
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
			"/docs/index.html", "/common/*.html", "/jwt-test", "/api/v1/auth/**"
	};

	private final JwtService jwtTokenProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

	/*
	 * CORS 설정
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedOrigin("https://www.ddoeat.site");
		config.addAllowedHeader("*");
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.addExposedHeader("Authorization");
		config.addExposedHeader("Authorization-refresh");
		config.addExposedHeader("Set-Cookie");
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;

	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) //csrf 비활성
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.formLogin(AbstractHttpConfigurer::disable) //폼 로그인 비활성
			.httpBasic(AbstractHttpConfigurer::disable) //HTTP 기본인증 비활성
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(request -> request
				.requestMatchers(PATTERNS).permitAll()
				.requestMatchers("/api/v1/auth/signup").hasRole("GUEST")
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
			.addFilterBefore(new CustomAuthenticationRequestFilter(), SecurityContextHolderFilter.class)
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
