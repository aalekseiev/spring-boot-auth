package com.auth0.samples.authapi.security;

import javax.servlet.http.Cookie;

public class HttpOnlySecureCookie extends Cookie {

	private static final long serialVersionUID = 1L;
	
	public HttpOnlySecureCookie(String name, String value) {
		super(name, value);
		setHttpOnly(true);
		setSecure(true);
	}

}
