package com.depromeet.auth.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.depromeet.auth.service.RedisService;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

	private Key key;

	private final RedisService redisService;
	private final UserRepository userRepository;

	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	/*
	AccessToken 생성
	 */
	public String createAccessToken(Long userId) {
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
	public String createRefreshToken(Long userId) {
		var now = new Date();
		String refreshToken = Jwts.builder()
			.setSubject(REFRESH_TOKEN_SUBJECT)
			.claim("userId", userId)
			.setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		redisService.setValues(String.valueOf(userId), refreshToken, refreshTokenExpirationPeriod);
		return refreshToken;
	}

	/*
	토큰 유효성 검사
	 */
	public boolean isValidToken(String token) {
		try {
			Claims claims = getClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			throw new CustomException(Result.TOKEN_EXPIRED);
		} catch (SecurityException | MalformedJwtException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		} catch (UnsupportedJwtException e) {
			throw new CustomException(Result.TOKEN_NOTSUPPORTED);
		} catch (JwtException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (JwtException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}

	public Authentication getAuthentication(String token) {
		User user = getUserFromToken(token);
		return new UsernamePasswordAuthenticationToken(
			user,
			null,
			getAuthorities(user)
		);
	}

	public Long getUserIdFromToken(String token) {
		return Long.valueOf(getClaims(token).get("userId", Integer.class));
	}

	private Collection<GrantedAuthority> getAuthorities(User user) {
		return Collections.singletonList(
			new SimpleGrantedAuthority("ROLE_" + user.getUserRole().toString())
		);
	}

	private User getUserFromToken(String token) {
		Claims claims = getClaims(token);
		Long userId = Long.valueOf(claims.get("userId", Integer.class));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		if (user.getDeletedAt() != null) {
			throw new CustomException(Result.DELETED_USER);
		}
		return user;
	}

	public Long getLeftAccessTokenTTLSecond(String token) {
		return getClaims(token).getExpiration().getTime();
	}
}
