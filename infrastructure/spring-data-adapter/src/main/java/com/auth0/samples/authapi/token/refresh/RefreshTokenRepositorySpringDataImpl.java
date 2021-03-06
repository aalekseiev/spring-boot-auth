package com.auth0.samples.authapi.token.refresh;

import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenRepositorySpringDataImpl extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByTokenId(String tokenId);
	
}
