package com.essalud.gcpp.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.essalud.gcpp.entidades.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	public static final long JWT_TOKEN_VALIDITY =7200000; //Último bloque: Nro de minutos
	
	@Value("${jwt.secret}")
	private String secret;
	
	// OBTENER USUARIO 
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	// OBTENER EXPIRACIÓN
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	// OBTENER TODOS LOS CLAIMS
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
	}
		
		
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	// -----
	// GENERAR TOKEN PARA USUARIO
	public String generateToken(Users user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, user);
	}
	
	// GENERAR TOKEN
	private String doGenerateToken(Map<String, Object> claims, Users user) {
		//List<GrantedAuthority> grantedAuthorities =  new ArrayList<>(user.getAuthorities());
		return Jwts.builder()
				.setClaims(claims)
				.claim("user_id", user.getUsername())
				.claim("area", user.getArea())
				.claim("displayname", user.getDisplayName())
				.claim("title", user.getSubtitle())
				.claim("regimen", user.getRegimen())
				.claim("expired", user.isCredentialExpired())
				.claim("ap", user.isAcceptedPolicies())
				.claim("roles", user.getAuthorities())
				.claim("id", user.getId())
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY ))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}
}
