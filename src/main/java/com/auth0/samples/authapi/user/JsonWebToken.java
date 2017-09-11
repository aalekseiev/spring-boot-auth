package com.auth0.samples.authapi.user;

import static com.auth0.samples.authapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JsonWebToken {

    private final String userName;

    private final Collection<? extends GrantedAuthority> authorities;
    
    private final CsrfToken csrfToken;

    public JsonWebToken(String userName, Collection<? extends GrantedAuthority> authorities, CsrfToken csrfToken) {
        super();
        this.userName = userName;
        this.authorities = authorities;
        this.csrfToken = csrfToken;
    }
    
    public String userName() {
        return this.userName;
    }

    public String toString() {
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("xsrfToken", csrfToken.toString())
                .claim("permissions", authorities
                                    .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .map(String::trim)
                                    .collect(Collectors.toList()))
                .compact();
    }
}
