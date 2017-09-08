package com.auth0.samples.authapi.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.samples.authapi.security.SecurityConstants.JWT_HEADER_STRING;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;
import static com.auth0.samples.authapi.security.SecurityConstants.JWT_TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
//        String header = request.getHeader(JWT_HEADER_STRING);
        
        String authorizationCookie = null;
    	if (request.getCookies() != null) {
	    	for (Cookie cookie : request.getCookies()) {
	    		if (SecurityConstants.JWT_HEADER_STRING.equals(cookie.getName())) {
	    			authorizationCookie = cookie.getValue();
	    			break;
	    		}
	    	}
    	}

        if (authorizationCookie == null/* || !header.startsWith(JWT_TOKEN_PREFIX)*/) {
            chain.doFilter(request, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = null;
    	if (request.getCookies() != null) {
	    	for (Cookie cookie : request.getCookies()) {
	    		if (SecurityConstants.JWT_HEADER_STRING.equals(cookie.getName())) {
	    			token = cookie.getValue();
	    			break;
	    		}
	    	}
    	}
        
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(JWT_TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}