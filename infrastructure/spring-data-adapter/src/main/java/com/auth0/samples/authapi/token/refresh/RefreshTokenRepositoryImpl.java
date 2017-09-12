package com.auth0.samples.authapi.token.refresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final RefreshTokenRepositorySpringDataImpl repo;
	
	@Autowired
	public RefreshTokenRepositoryImpl(RefreshTokenRepositorySpringDataImpl repo) {
		this.repo = repo;
	}
	
	@Override
	public RefreshToken findByTokenId(String tokenId) {
		return repo.findByTokenId(tokenId);
	}

	@Override
	public void delete(long id) {
		repo.delete(id);
	}

	@Override
	public void save(RefreshToken refreshToken) {
		repo.save(refreshToken);
	}

}
