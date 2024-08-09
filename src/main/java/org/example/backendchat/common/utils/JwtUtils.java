package org.example.backendchat.common.utils;

import static org.example.backendchat.common.constants.JwtConstants.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

	@Value("${spring.jwt.access-token-expiration}")
	private Long accessTokenExpTime;

	@Value("${spring.jwt.refresh-token-expiration}")
	private Long refreshTokenExpTime;

	private SecretKey secretKey;

	public JwtUtils(@Value("${spring.jwt.secret}") String secretKey) {
		this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getEmail(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("email", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}

	public String getLoginType(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("loginType", String.class);
	}

	public String getTokenType(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("tokenType", String.class);
	}

	public Long getRefreshTokenExpTimeByToken(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.getTime();
	}

	public Long getAccessTokenExpTime() {
		return this.accessTokenExpTime;
	}

	public Long getRefreshTokenExpTime() {
		return this.refreshTokenExpTime;
	}

	public Boolean isExpired(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	public String parsingAccessToken(String token) {
		return token.substring(7);
	}

	public String createJwtAccessToken(String email, String role, String loginType) {
		return "Bearer " + Jwts.builder()
			.claim("email", email)
			.claim("role", role)
			.claim("loginType", loginType)
			.claim("tokenType", ACCESS_TOKEN.getMessage())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + getAccessTokenExpTime()))
			.signWith(secretKey)
			.compact();
	}

	public String createJwtRefreshToken(String email, String role, String loginType) {
		return Jwts.builder()
				.claim("email", email)
				.claim("role", role)
				.claim("loginType", loginType)
				.claim("tokenType", REFRESH_TOKEN.getMessage())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + getRefreshTokenExpTime()))
				.signWith(secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		return getClaims(token) != null;
	}

	private Jws<Claims> getClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseClaimsJws(token);
		} catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
				 IllegalArgumentException e) {
			return null;
		}
	}

}
