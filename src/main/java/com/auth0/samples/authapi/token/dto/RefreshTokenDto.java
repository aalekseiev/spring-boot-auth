package com.auth0.samples.authapi.token.dto;

public class RefreshTokenDto {
	
	private String tokenId;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String toString() {
		return "RefreshTokenDto [tokenId=" + tokenId + "]";
	}
	
}
