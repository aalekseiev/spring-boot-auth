package com.auth0.samples.authapi.token;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.auth0.samples.authapi.user.TokenIdSource;

@Entity
@Configurable
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String tokenId;

	private String body;

	private String userId;
	
	private LocalDateTime validUntil;
	
	@Autowired
	@Transient
	private RefreshTokenRepository repo;
	
	protected RefreshToken() {
	    
	}
	
	public RefreshToken(String tokenId, String body, String userName) {
	    this.tokenId = tokenId;
	    this.body = body;
        this.userId = userName;
        this.validUntil = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    
    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    
    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
    
//    @Transactional
    public RefreshToken refresh() {
        repo.delete(id);
        if (validUntil.isAfter(LocalDateTime.now())) {
            RefreshToken refreshToken = new RefreshToken(new TokenIdSource().generatedId(), "body-not-implemented-yet", userId);
            repo.save(refreshToken);
            return refreshToken;
        } else {
            throw new RuntimeException("Refresh Token with id=" + tokenId + " has expired");
        }
    }

    public void persist() {
        repo.save(this);        
    }
	
}
