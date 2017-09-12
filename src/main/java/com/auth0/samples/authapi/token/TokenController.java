package com.auth0.samples.authapi.token;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.samples.authapi.security.SecurityConstants;
import com.auth0.samples.authapi.token.dto.RefreshTokenDto;
import com.auth0.samples.authapi.token.dto.TokensDto;
import com.auth0.samples.authapi.token.jwt.JsonWebToken;
import com.auth0.samples.authapi.token.jwt.JwtTimingInfo;
import com.auth0.samples.authapi.token.jwt.JwtUserRelatedParameters;
import com.auth0.samples.authapi.token.refresh.RefreshToken;
import com.auth0.samples.authapi.token.refresh.RefreshTokenService;
import com.auth0.samples.authapi.user.dto.ApplicationUserDto;

import java.security.PrivateKey;
import java.security.PublicKey;

@RestController
@RequestMapping("/token")
public class TokenController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

    private AuthenticationManager authenticationManager;
    
    private RefreshTokenService refreshTokenService;
    
    private UserDetailsService userDetailsService;

    @Autowired
    private PrivateKey privateKey;

    @Autowired
    private PublicKey publicKey;

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
    public ResponseEntity<TokensDto> tokenObtain(@RequestBody ApplicationUserDto userDto, HttpServletRequest request) {
    	
    	System.out.println("Trying to log in as user: " + userDto);
    	
    	ResponseEntity<TokensDto> retVal = null;
    	
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		userDto.getUsername(),
                		userDto.getPassword())
        );
    	
    	if (authentication != null) {
    	    final CsrfToken csrfToken = new CsrfToken();
    	    
    	    final JsonWebToken jwt = new JsonWebToken(
    	    						     new JwtUserRelatedParameters(
    	    						         authentication.getName(),
    	    						         authentication.getAuthorities(),
    	    						         request.getRemoteAddr()
    	    						     ),
    	    						     csrfToken.toString(),
    	    						     new JwtTimingInfo(
    	    						         System.currentTimeMillis(),
    	    						         SecurityConstants.EXPIRATION_TIME
    	    						     ),
										privateKey,
										publicKey
    	    						  );
    	    
    	    final RefreshToken refreshToken = new RefreshToken(
    	    								      new TokenIdSource().generatedId(),
    	    								      jwt.tokenId(),
    	    								      jwt.ip(),
    	    								      "body-not-implemented-yet",
    	    								      authentication.getName()
    	    								  );
            refreshToken.persist();
    	    
    	    TokensDto result = new TokensDto(
    	    				           jwt.toString(),
    	    				           refreshToken.getTokenId(),
    	    				           csrfToken.toString(),
    	    				           jwt.expiresIn() / 1000,
    	    				           refreshToken.expiresIn() / 1000
    	    				   );
    	    retVal = new ResponseEntity<>(result, HttpStatus.OK);
    	} else {
    		retVal = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    	return retVal;
    }
    
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<TokensDto> tokenRefresh(
                                         @CookieValue("Authorization") String authorizationCookie,
                                         @RequestBody RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
    	
    	System.out.println("Trying to refresh token: " + refreshTokenDto);
    	
    	ResponseEntity<TokensDto> retVal = null;
    	
    	RefreshToken refreshToken = refreshTokenService.findByTokenId(refreshTokenDto.getTokenId());
    	
    	if (refreshToken != null) {
    		UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUserId());

    		final CsrfToken csrfToken = new CsrfToken();
    		
    		final JsonWebToken currentJwt = JsonWebToken.ofStringIgnoringExpiration(authorizationCookie, privateKey, publicKey);
    		
    		if (!refreshToken.isConsistentWith(currentJwt)) {
    			throw new RuntimeException("Provided JWT.rti and RefreshToken's id are not consistent");
    		}
    		
    		final JsonWebToken jwt = new JsonWebToken(
    								     new JwtUserRelatedParameters(
    								         user.getUsername(),
    								         user.getAuthorities(),
    								         request.getRemoteAddr()
    								     ),
				    					csrfToken.toString(),
				    					new JwtTimingInfo(
				    					    System.currentTimeMillis(),
				    					    SecurityConstants.EXPIRATION_TIME
				    					),
										privateKey,
					publicKey);
    		    		
    		RefreshToken newRefreshToken = null;
    		try {
    		    newRefreshToken = refreshToken.nextRefreshToken(jwt.tokenId(), jwt.ip());
    		    TokensDto result = new TokensDto(
    		    				           jwt.toString(),
    		    				           newRefreshToken.getTokenId(),
    		    				           csrfToken.toString(),
    		    				           jwt.expiresIn() / 1000,
    		    				           newRefreshToken.expiresIn() / 1000
    		    				   );
        		retVal = new ResponseEntity<>(result, HttpStatus.OK);
    		} catch (RuntimeException e) {
    		    LOG.error("Failed to refresh token: {}", e, refreshToken.getTokenId());
    		    retVal = new ResponseEntity<>(HttpStatus.FORBIDDEN);
    		}
    		
    	} else {
    	    LOG.error("Unable to find refresh token: {}", refreshTokenDto.getTokenId());
    	    retVal = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	return retVal;
    }

}
