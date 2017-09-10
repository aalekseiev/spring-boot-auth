package com.auth0.samples.authapi.user.dto;

public class ApplicationUserDto {

	private long id;
	private String username;
	private String password;

	private String permissions;

	protected ApplicationUserDto() {

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

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "ApplicationUserDto [id=" + id + ", username=" + username + ", password=" + password + ", permissions="
				+ permissions + "]";
	}

}