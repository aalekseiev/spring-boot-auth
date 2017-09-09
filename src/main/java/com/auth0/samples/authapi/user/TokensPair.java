package com.auth0.samples.authapi.user;

import org.springframework.security.web.csrf.CsrfToken;

public class TokensPair {

	private String jwt;

	private String refreshToken;

	private CsrfToken csrfToken;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public CsrfToken getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(CsrfToken csrfToken) {
		this.csrfToken = csrfToken;
	}

	protected TokensPair() {

	}

	public TokensPair(String jwt, String refreshToken, CsrfToken csrfToken) {
		super();
		this.jwt = jwt;
		this.refreshToken = refreshToken;
		this.csrfToken = csrfToken;
	}

}
