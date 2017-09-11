package com.auth0.samples.authapi.token.dto;

public class RefreshTokenDto {

	private String jwt;
	
	private String tokenId;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

    public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	@Override
	public String toString() {
		return "RefreshTokenDto [jwt=" + jwt + ", tokenId=" + tokenId + "]";
	}
	
}
