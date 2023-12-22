package com.depromeet.auth.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.depromeet.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

	private Key key;

	private final UserRepository userRepository;

	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/*
	AccessToken 생성
	 */
	public String createAccessToken(String userId) {
		Date now = new Date();
		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.claim("userId", userId)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
			.compact();
	}

	/*
	RefreshToken 생성
	 */
	public String createRefreshToken(String userId) {
		var now = new Date();
		return Jwts.builder()
			.setSubject(REFRESH_TOKEN_SUBJECT)
			.setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/*
	토큰 유효성 검사
	 */
	public Boolean validateToken(String token) {
		Claims claims = parseToken(token);
		return claims.getExpiration().getTime() >= new Date().getTime();
	}

	public Authentication getAuthentication(String token) {
		return new UsernamePasswordAuthenticationToken(
			getUserFromToken(token),
			null,
			getAuthorities(token)
		);
	}

	private Collection<GrantedAuthority> getAuthorities(String token) {
		Claims claims = parseToken(token);
		return Collections.singletonList(
			new SimpleGrantedAuthority(Role.USER.toString())
		);
	}

	private Claims parseToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private User getUserFromToken(String token) {
		Claims claims = parseToken(token);
		return userRepository.findById(Long.valueOf(claims.get("userId", String.class))).orElseThrow(() -> new CustomException(Result.FAIL));
	}
}
