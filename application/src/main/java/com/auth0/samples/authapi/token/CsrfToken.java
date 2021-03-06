package com.auth0.samples.authapi.token;

public final class CsrfToken {

    private final TokenIdSource tokenIdSource = new TokenIdSource();
    
    public String toString() {
        return tokenIdSource.generatedId();
    }
    
}
