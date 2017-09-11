package com.auth0.samples.authapi.user;

import static com.auth0.samples.authapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.samples.authapi.token.RefreshToken;
import com.auth0.samples.authapi.token.RefreshTokenRepository;
import com.auth0.samples.authapi.token.dto.RefreshTokenDto;
import com.auth0.samples.authapi.user.dto.ApplicationUserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/token")
public class TokenController {

    private AuthenticationManager authenticationManager;
    
    private RefreshTokenRepository refreshTokenRepository;
    
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenController(AuthenticationManager authenticationManager,
    					   RefreshTokenRepository refreshTokenRepository,
    					   UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
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
    		TokensPair result = createTokenPair(authentication.getName(), authentication.getAuthorities());
			return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

	private TokensPair createTokenPair(String userName, Collection<? extends GrantedAuthority> collection) {
		final String csrfToken = UUID.randomUUID().toString().replace("-", "");
		String token = Jwts.builder()
		        .setSubject(userName)
		        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
		        .signWith(SignatureAlgorithm.HS512, SECRET)
		        .claim("xsrfToken", csrfToken)
		        .claim("permissions", collection
		        					.stream()
		        					  .map(GrantedAuthority::getAuthority)
		        					  .map(String::trim)
		        					.collect(Collectors.toList()))
		        .compact();

		RefreshToken rt = new RefreshToken();
		rt.setBody("body-not-implemented-yet");
		rt.setUserId(userName);
		rt.setTokenId(UUID.randomUUID().toString());
		refreshTokenRepository.save(rt);
		
		TokensPair result = new TokensPair(token, rt.getTokenId(), csrfToken);
		return result;
	}
    
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<TokensPair> tokenRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
    	
    	System.out.println("Trying to refresh token: " + refreshTokenDto);
    	
    	RefreshToken refreshToken = refreshTokenRepository.findByTokenId(refreshTokenDto.getTokenId());
    	
    	if (refreshToken != null) {
    		UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUserId());
    		
    		TokensPair result = createTokenPair(user.getUsername(), user.getAuthorities());
    		return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}