package com.auth0.samples.authapi.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.samples.authapi.user.dto.ApplicationUserDto;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    @ResponseBody
    public ResponseEntity<ApplicationUser> signUp(@RequestBody ApplicationUserDto userDto) {
    	ApplicationUser user = new ApplicationUser();
    	System.out.println("Signing up user: " + userDto);
    	user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        Permissions permissions = new Permissions();
        permissions.getPermissions().addAll(Arrays.asList(userDto.getPermissions().split(",")));
		user.setPermissions(permissions);
        applicationUserRepository.save(user);
        
        List<ApplicationUser> findAll = applicationUserRepository.findAll();
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}