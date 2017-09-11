package com.auth0.samples.authapi.token;

import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByTokenId(String tokenId);
	
}
