package com.auth0.samples.authapi.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public RefreshToken findByTokenId(String tokenId) {
        return repository.findByTokenId(tokenId);
    }

}
