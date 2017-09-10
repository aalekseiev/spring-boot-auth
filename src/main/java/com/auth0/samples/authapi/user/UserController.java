package com.auth0.samples.authapi.user;

import static com.auth0.samples.authapi.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.security.SecurityConstants.JWT_HEADER_STRING;
import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/sign-up")
    @ResponseBody
    public ResponseEntity<ApplicationUser> signUp(@RequestBody ApplicationUser user) {
    	System.out.println("Signing up user: " + user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PostMapping("/token/obtain")
    @ResponseBody
    public ResponseEntity<TokensPair> token(@RequestBody ApplicationUser user) {
    	
    	System.out.println("Trying to log in as user: " + user);
    	
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		user.getUsername(),
                		user.getPassword(),
                        new ArrayList<>())
        );

    	if (authentication != null) {
    		final String csrfToken = UUID.randomUUID().toString().replace("-", "");
			String token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .claim("xsrfToken", csrfToken)
                    .compact();

    		TokensPair result = new TokensPair(token, "refresh-token-is-not-implemented-yet", csrfToken);
			return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = "CSRFConfig".concat(".CSRF_TOKEN");

}