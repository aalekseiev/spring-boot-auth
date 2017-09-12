package com.auth0.samples.authapi.token.refresh;

public interface RefreshTokenService {

    public RefreshToken findByTokenId(String tokenId);
}
