package com.auth0.samples.authapi.token.dto;

public class TokensDto {

	private String jwt;

	private String refreshToken;

	private String csrfToken;

	private Long jwtExpiresIn;

	private Long refreshTokenExpiresIn;

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

	public Long getJwtExpiresIn() {
		return jwtExpiresIn;
	}

	public void setJwtExpiresIn(Long jwtExpiresIn) {
		this.jwtExpiresIn = jwtExpiresIn;
	}

	public Long getRefreshTokenExpiresIn() {
		return refreshTokenExpiresIn;
	}

	public void setRefreshTokenExpiresIn(Long refreshTokenExpiresIn) {
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

	protected TokensDto() {

	}

	public TokensDto(
			   String jwt,
			   String refreshToken,
			   String csrfToken,
			   Long jwtExpiresIn,
			   Long refreshTokenExpiresIn) {
		super();
		this.jwt = jwt;
		this.refreshToken = refreshToken;
		this.csrfToken = csrfToken;
		this.jwtExpiresIn = jwtExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

}
