package com.auth0.samples.authapi.user;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

	private List<String> permissions = new ArrayList<>();
	
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
