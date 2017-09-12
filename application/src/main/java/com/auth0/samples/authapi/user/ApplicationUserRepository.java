package com.auth0.samples.authapi.user;

import java.util.List;

public interface ApplicationUserRepository {

	ApplicationUser findByUsername(String username);

	void save(ApplicationUser user);

}
