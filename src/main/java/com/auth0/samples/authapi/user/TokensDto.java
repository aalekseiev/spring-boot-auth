package com.auth0.samples.authapi.user;

public class TokensDto {

	private String jwt;

	private String refreshToken;

	private String csrfToken;

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

	public String getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

	protected TokensDto() {

	}

	public TokensDto(String jwt, String refreshToken, String csrfToken) {
		super();
		this.jwt = jwt;
		this.refreshToken = refreshToken;
		this.csrfToken = csrfToken;
	}

}
