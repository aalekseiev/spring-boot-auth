package com.auth0.samples.authapi.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 60_000; // 1 minutes
    public static final String JWT_HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    
    public static final String CSRF_TOKEN_HEADER = "XSRF-TOKEN";
}
