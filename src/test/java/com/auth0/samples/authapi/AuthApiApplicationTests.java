package com.auth0.samples.authapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.auth0.samples.authapi.security.WebSecurity;

//import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import javax.servlet.Filter;

import org.hamcrest.CoreMatchers;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
public class AuthApiApplicationTests {

    private MockMvc mvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private Filter springSecurityFilterChain;

	@Before
    public void setup() throws Exception {
        this.mvc = webAppContextSetup(webApplicationContext)
        		.addFilters(springSecurityFilterChain)
        		.build();
    }
	
	@Test
	public void testLogin() throws Exception {
		mvc.perform(post("/users/sign-up")
				    .contentType("application/json")
				    .content(new JsonFile("admin_user.json").content()))
		.andExpect(status().isOk());
		
		mvc.perform(post("/login")
					.contentType("application/json")
					.content(new JsonFile("admin_user.json").content()))
		.andExpect(status().isOk())
		.andExpect(header().string("Authorization", startsWith("Bearer ")))
		.andExpect(header().string("XSRF-TOKEN", notNullValue()));
	}

}
