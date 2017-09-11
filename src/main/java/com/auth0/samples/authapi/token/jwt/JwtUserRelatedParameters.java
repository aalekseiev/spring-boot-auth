package com.auth0.samples.authapi.token.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public final class JwtUserRelatedParameters {

	private final String name;

	private final Collection<? extends GrantedAuthority> authorities;
	
	private final String ip;

	public JwtUserRelatedParameters(String name, Collection<? extends GrantedAuthority> authorities, String ip) {
		super();
		this.name = name;
		this.authorities = authorities;
		this.ip = ip;
	}

	public String name() {
		return this.name;
	}
	
	public Collection<? extends GrantedAuthority> authorities() {
		return this.authorities;
	}
	
	public String ip() {
		return this.ip;
	}
}
