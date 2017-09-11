package com.auth0.samples.authapi.user;

import static com.auth0.samples.authapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.samples.authapi.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JsonWebToken {
	
	private final String tokenId;

    private final String userName;

    private final Collection<? extends GrantedAuthority> authorities;
    
    private final String csrfToken;
    
    private final String ip;

    public JsonWebToken(String userName, 
    		Collection<? extends GrantedAuthority> authorities, 

    		String csrfToken, 
    		String ip) {
        super();
        this.tokenId = new TokenIdSource().generatedId();
        this.userName = userName;
        this.authorities = authorities;
        this.csrfToken = csrfToken;
        this.ip = ip;
    }
    
    public JsonWebToken(String tokenId, String userName, 
    		Collection<? extends GrantedAuthority> authorities, 

    		String csrfToken, 
    		String ip) {
        super();
        this.tokenId = tokenId;
        this.userName = userName;
        this.authorities = authorities;
        this.csrfToken = csrfToken;
        this.ip = ip;
    }
    
    public String tokenId() {
    	return this.tokenId;
    }
    
    public String userName() {
        return this.userName;
    }
    
    public String toString() {
        long curDateTime = System.currentTimeMillis();
		return Jwts.builder()
                .setSubject(userName)
                .setId(tokenId)
                .setAudience("rightway.run")
                .setIssuer("rightway.run")
                .setIssuedAt(new Date(curDateTime))
                .setExpiration(new Date(curDateTime  + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("ip", ip)
                .claim("xsrfToken", csrfToken)
                .claim("permissions", authorities
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
		return new JsonWebToken(claims.getId(), claims.getSubject(),
				(Collection<? extends GrantedAuthority>)permissions,
				claims.get("csrfToken", String.class),
				claims.get("ip", String.class));
	}

}
