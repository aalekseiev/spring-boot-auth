package com.auth0.samples.authapi.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    
    @Column
    private Permissions permissions;

    protected ApplicationUser() {
    	
    }
    
    public ApplicationUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}

}