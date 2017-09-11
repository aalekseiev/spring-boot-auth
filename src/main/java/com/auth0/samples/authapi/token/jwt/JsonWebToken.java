package com.auth0.samples.authapi.token.jwt;

import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

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
	
	private final String tokenId;
	
	private final JwtUserRelatedParameters userInfo;

    private final String csrfToken;
    
    private final JwtTimingInfo timingInfo;

    public JsonWebToken(JwtUserRelatedParameters jwtUserInfo, 
    		String csrfToken,
    		JwtTimingInfo jwtTimingInfo) {
        this(jwtUserInfo, new TokenIdSource().generatedId(), csrfToken, jwtTimingInfo);
    }
    
    public JsonWebToken(JwtUserRelatedParameters jwtUserInfo,
    		String tokenId,
    		String csrfToken,
    		JwtTimingInfo jwtTimingInfo) {
        super();
        this.tokenId = tokenId;
        this.userInfo = jwtUserInfo;
        this.csrfToken = csrfToken;
        this.timingInfo = jwtTimingInfo;
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
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("ip", userInfo.ip())
                .claim("xsrfToken", csrfToken)
                .claim("permissions", userInfo.authorities()
                                    .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .map(String::trim)
                                    .collect(Collectors.toList()))
                .compact();
    }

	public static JsonWebToken ofStringIgnoringExpiration(String jwtString) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
	                .setSigningKey(SecurityConstants.SECRET)
	                .parseClaimsJws(jwtString).getBody();
			
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			claims = e.getClaims();
		}
		
		List<GrantedAuthority> permissions = new ArrayList<>();
		for (Object curPermission : claims.get("permissions", List.class)) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority((String) curPermission);
			permissions.add(grantedAuthority);
		}
		return new JsonWebToken(
			       new JwtUserRelatedParameters(
				       claims.getSubject(),
					   (Collection<? extends GrantedAuthority>)permissions,
					   claims.get("ip", String.class)
				   ),
			       claims.getId(),
			       claims.get("csrfToken", String.class),
			       new JwtTimingInfo(
				       claims.getIssuedAt().getTime(),
				       claims.getExpiration().getTime() - claims.getIssuedAt().getTime()
				   )
				);
	}

}
