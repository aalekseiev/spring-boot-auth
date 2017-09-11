package com.auth0.samples.authapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.samples.authapi.token.RefreshToken;
import com.auth0.samples.authapi.token.RefreshTokenService;
import com.auth0.samples.authapi.token.dto.RefreshTokenDto;
import com.auth0.samples.authapi.user.dto.ApplicationUserDto;

@RestController
@RequestMapping("/token")
public class TokenController {

    private AuthenticationManager authenticationManager;
    
    private RefreshTokenService refreshTokenService;
    
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenController(AuthenticationManager authenticationManager,
                           RefreshTokenService refreshTokenService,
    					   UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }
    
    @PostMapping("/obtain")
    @ResponseBody
    public ResponseEntity<TokensDto> tokenObtain(@RequestBody ApplicationUserDto userDto) {
    	
    	System.out.println("Trying to log in as user: " + userDto);
    	
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		userDto.getUsername(),
                		userDto.getPassword())
        );
    	
    	if (authentication != null) {
    	    final CsrfToken csrfToken = new CsrfToken();
    	    final JsonWebToken jwt = new JsonWebToken(authentication.getName(), authentication.getAuthorities(), csrfToken);
    	    RefreshToken refreshToken = new RefreshToken(new TokenIdSource().generatedId(), "body-not-implemented-yet", jwt.userName());
            refreshToken.persist();

    	    TokensDto result = new TokensDto(jwt.toString(), refreshToken.getTokenId(), csrfToken.toString());
			return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<TokensDto> tokenRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
    	
    	System.out.println("Trying to refresh token: " + refreshTokenDto);
    	
    	RefreshToken refreshToken = refreshTokenService.findByTokenId(refreshTokenDto.getTokenId());
    	
    	if (refreshToken != null) {
    		UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUserId());

    		final CsrfToken csrfToken = new CsrfToken();
    		final JsonWebToken jwt = new JsonWebToken(user.getUsername(), user.getAuthorities(), csrfToken);
    		RefreshToken newRefreshToken = null;
    		try {
    		    newRefreshToken = refreshToken.refresh();
    		} catch (RuntimeException e) {
    		    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    		}
    		
    		TokensDto result = new TokensDto(jwt.toString(), newRefreshToken.getTokenId(), csrfToken.toString());
    		return new ResponseEntity<>(result, HttpStatus.OK);
    	} else {
    	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

}