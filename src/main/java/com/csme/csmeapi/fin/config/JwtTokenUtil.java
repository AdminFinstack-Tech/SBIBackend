package com.csme.csmeapi.fin.config;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.csme.csmeapi.fin.models.LoginRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String extendTokenExpiration(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        // Check if token is expired
        if (isTokenExpired(token)) {
            // Extend the expiration date
            final Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);
            // Update token's expiration date in claims
            claims.setExpiration(expirationDate);
            // Re-generate token with updated expiration date
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
        // If token is not expired, return the original token
        return token;
    }
	
	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public String getUnitCodeFromToken(String token) {
		return getClaimFromToken(token, claims -> claims.get("CorporateId", String.class));
	}

	public String getSecretFromToken(String token) {
		return getClaimFromToken(token, claims -> claims.get("SecretKey", String.class));
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateRefreshToken(@Valid LoginRequest body, String secertKey) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, body, secertKey);
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token) {
		final String secret = getSecretFromToken(token);
		return (secret.equals(secret) && !isTokenExpired(token));
	}

	public String generateToken(@Valid LoginRequest body, String secertKey) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, body, secertKey);
	}

	private String doGenerateToken(Map<String, Object> claims, @Valid LoginRequest body, String secertKey) throws AuthenticationException {
		String key;
		try {
			Date now = new Date();
			Date expiryDate = new Date(now.getTime() + expiration);
			key = null;
			if(secertKey.equals(secret)) {
				key =  Jwts.builder()
						.setClaims(claims)
						.claim("UserId", body.getUserId())
						.claim("CorporateId", body.getCorporateId())
						.claim("SecretKey", secertKey)
						.setSubject(body.getUserId())
						.setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(expiryDate)
						.signWith(SignatureAlgorithm.HS512, secertKey).compact();
			} else {
				throw new AuthenticationCredentialsNotFoundException("No free port found");
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new AuthenticationCredentialsNotFoundException("Secret key is invalid "+e);
		}
		return key;

	}

}
