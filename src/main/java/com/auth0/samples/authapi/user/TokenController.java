package com.auth0.samples.authapi.user;

import static com.auth0.samples.authapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.samples.authapi.user.dto.ApplicationUserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/token")
public class TokenController {

    private AuthenticationManager authenticationManager;

    @Autowired
    public TokenController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @PostMapping("/obtain")
    @ResponseBody
    public ResponseEntity<TokensPair> tokenObtain(@RequestBody ApplicationUserDto userDto) {
    	
    	System.out.println("Trying to log in as user: " + userDto);
    	
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		userDto.getUsername(),
                		userDto.getPassword())
        );
    	
    	if (authentication != null) {
    		final String csrfToken = UUID.randomUUID().toString().replace("-", "");
			String token = Jwts.builder()
                    .setSubject(authentication.getName())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .claim("xsrfToken", csrfToken)
                    .claim("permissions", authentication.getAuthorities()
                    					.stream()
                    					  .map(GrantedAuthority::getAuthority)
                    					  .map(String::trim)
                    					.collect(Collectors.toList()))
                    .compact();

    		TokensPair result = new TokensPair(token, "refresh-token-is-not-implemented-yet", csrfToken);
			return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}