package com.auth0.samples.authapi.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

	private final ApplicationUserRepositorySpringDataImpl repo;
	
	@Autowired
	public ApplicationUserRepositoryImpl(ApplicationUserRepositorySpringDataImpl repo) {
		this.repo = repo;
	}
	
	@Override
	public ApplicationUser findByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Override
	public void save(ApplicationUser user) {
		repo.save(user);
	}

}
