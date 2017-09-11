package com.auth0.samples.authapi.token.jwt;

public class JwtTimingInfo {

	private final Long issuingDateTime;

	private final Long ttlInMillis;

	public JwtTimingInfo(Long issuingDateTime, Long ttlInMillis) {
		super();
		this.issuingDateTime = issuingDateTime;
		this.ttlInMillis = ttlInMillis;
	}

	public Long issuingDateTime() {
		return this.issuingDateTime;
	}
	
	public Long expirationDateTime() {
		return this.issuingDateTime + ttlInMillis;
	}
}
