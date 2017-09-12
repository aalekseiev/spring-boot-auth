package com.auth0.samples.authapi.token.jwt;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.samples.authapi.security.SecurityConstants;
import com.auth0.samples.authapi.token.TokenIdSource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JsonWebToken {
	
	private static final String IP = "ip";

	private static final String PERMISSIONS = "permissions";

	private static final String XSRF_TOKEN = "xsrfToken";

	private final String tokenId;
	
	private final JwtUserRelatedParameters userInfo;

    private final String csrfToken;
    
    private final JwtTimingInfo timingInfo;

	private final PrivateKey privateRsaKey;
	private PublicKey publicKey;

	public JsonWebToken(JwtUserRelatedParameters jwtUserInfo,
						String csrfToken,
						JwtTimingInfo jwtTimingInfo, PrivateKey privateRsaKey, PublicKey publicKey) {
        this(jwtUserInfo, new TokenIdSource().generatedId(), csrfToken, jwtTimingInfo, privateRsaKey, publicKey);
	}
    
    public JsonWebToken(JwtUserRelatedParameters jwtUserInfo,
						String tokenId,
						String csrfToken,
						JwtTimingInfo jwtTimingInfo, PrivateKey privateRsaKey, PublicKey publicKey) {
        super();
        this.tokenId = tokenId;
        this.userInfo = jwtUserInfo;
        this.csrfToken = csrfToken;
        this.timingInfo = jwtTimingInfo;
		this.privateRsaKey = privateRsaKey;
		this.publicKey = publicKey;
	}
    
    public String tokenId() {
    	return this.tokenId;
    }
    
    public String userName() {
        return this.userInfo.name();
    }
    
    public String ip() {
    	return this.userInfo.ip();
    }
    
    public String toString() {
		return Jwts.builder()
                .setSubject(userInfo.name())
                .setId(tokenId)
                .setAudience("rightway.run")
                .setIssuer("rightway.run")
                .setIssuedAt(new Date(timingInfo.issuingDateTime()))
                .setExpiration(new Date(timingInfo.expirationDateTime()))
				.signWith(SignatureAlgorithm.RS512, privateRsaKey)
                .claim(IP, userInfo.ip())
                .claim(XSRF_TOKEN, csrfToken)
                .claim(PERMISSIONS, userInfo.authorities()
                                    .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .map(String::trim)
                                    .collect(Collectors.toList()))
                .compact();
    }

	public static JsonWebToken ofStringIgnoringExpiration(String jwtString, PrivateKey privateKey, PublicKey publicKey) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
//	                .setSigningKey(SecurityConstants.SECRET)
					.setSigningKey(publicKey)
	                .parseClaimsJws(jwtString).getBody();
			
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			claims = e.getClaims();
		}
		
		List<GrantedAuthority> permissions = new ArrayList<>();
		for (Object curPermission : claims.get(PERMISSIONS, List.class)) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority((String) curPermission);
			permissions.add(grantedAuthority);
		}
		return new JsonWebToken(
			       new JwtUserRelatedParameters(
				       claims.getSubject(),
					   (Collection<? extends GrantedAuthority>)permissions,
					   claims.get(IP, String.class)
				   ),
			       claims.getId(),
			       claims.get(XSRF_TOKEN, String.class),
			       new JwtTimingInfo(
				       claims.getIssuedAt().getTime(),
				       claims.getExpiration().getTime() - claims.getIssuedAt().getTime()
				   ), privateKey, publicKey);
	}

}
