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
        StringBuilder builder = new StringBuilder();
        builder.append("RefreshTokenDto [");
        if (tokenId != null) {
            builder.append("tokenId=");
            builder.append(tokenId);
        }
        builder.append("]");
        return builder.toString();
    }
	
}
