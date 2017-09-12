package com.auth0.samples.authapi.user;

public interface ApplicationUserRepository {

	ApplicationUser findByUsername(String username);

	void save(ApplicationUser user);

}
