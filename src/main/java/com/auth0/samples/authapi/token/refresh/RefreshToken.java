package com.auth0.samples.authapi.token.refresh;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.auth0.samples.authapi.token.TokenIdSource;
import com.auth0.samples.authapi.token.jwt.JsonWebToken;

@Entity
@Configurable
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String tokenId;
	
	private String jwtId;
	
	private String jwtIp;

	private String body;

	private String userId;
	
	private LocalDateTime validUntil;
	
	@Autowired
	@Transient
	private RefreshTokenRepository repo;
	
	protected RefreshToken() {
	    
	}
	
	public RefreshToken(String tokenId, String jwtId, String jwtIp, String body, String userName) {
	    this(tokenId, jwtId, jwtIp, body, userName, 3 * 60);
    }
	
	public RefreshToken(String tokenId, String jwtId, String jwtIp, String body, String userName, Integer ttlMinutes) {
	    this.tokenId = tokenId;
	    this.jwtId = jwtId;
	    this.jwtIp = jwtIp;
	    this.body = body;
        this.userId = userName;
        this.validUntil = LocalDateTime.now().plus(ttlMinutes, ChronoUnit.SECONDS);
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

	public String getJwtId() {
		return jwtId;
	}

	public void setJwtId(String jwtId) {
		this.jwtId = jwtId;
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
    
    public RefreshToken nextRefreshToken(String newJwtId, String newJwtIp) {
        repo.delete(id);
        if (LocalDateTime.now().isAfter(validUntil)) {
        	throw new RuntimeException("Refresh Token with id=" + tokenId + " has expired");
        } else if (!jwtIp.equals(newJwtIp)) {
            throw new RuntimeException("Trying to refresh token from ip: " + newJwtIp + " which was not used to create refresh token");
        } else {
        	RefreshToken refreshToken = new RefreshToken(
        								    new TokenIdSource().generatedId(),
        								    newJwtId,
        								    jwtIp,
        								    "body-not-implemented-yet",
        								    userId
        								);
            repo.save(refreshToken);
            return refreshToken;
        }
    }

    public void persist() {
        repo.save(this);        
    }

	public boolean isConsistentWith(JsonWebToken currentJwt) {
		return jwtId.equals(currentJwt.tokenId());
	}
	
}
