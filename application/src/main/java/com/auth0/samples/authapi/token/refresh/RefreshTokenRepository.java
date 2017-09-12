package com.auth0.samples.authapi.token.refresh;

public interface RefreshTokenRepository {

	RefreshToken findByTokenId(String tokenId);

	void delete(long id);

	void save(RefreshToken refreshToken);

}
