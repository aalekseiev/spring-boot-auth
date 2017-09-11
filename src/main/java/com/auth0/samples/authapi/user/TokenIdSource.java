package com.auth0.samples.authapi.user;

import java.util.UUID;

public class TokenIdSource {
    
    public String generatedId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
