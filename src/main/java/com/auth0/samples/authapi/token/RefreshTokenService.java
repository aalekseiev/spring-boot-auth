package com.auth0.samples.authapi.token;

public interface RefreshTokenService {

    public RefreshToken findByTokenId(String tokenId);
}
