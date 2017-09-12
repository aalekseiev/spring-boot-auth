package com.auth0.samples.authapi;


import com.auth0.samples.authapi.token.jwt.JsonWebToken;
import com.auth0.samples.authapi.token.jwt.JwtTimingInfo;
import com.auth0.samples.authapi.token.jwt.JwtUserRelatedParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthorizationServerApplication.class})
public class PrivateKeySigningTest {
	@Autowired
	private PrivateKey privateKey;

	@Autowired
	private PublicKey publicKey;

	@Test
	public void shouldSignJwtToken() {
		JwtUserRelatedParameters parameters = new JwtUserRelatedParameters("name1", new ArrayList<>(), "");
		JwtTimingInfo timingInfo = new JwtTimingInfo(System.currentTimeMillis(), 60000L);
		JsonWebToken jsonWebToken = new JsonWebToken(parameters, "123", timingInfo, privateKey);
		String jwt = jsonWebToken.toString();
		Assert.assertNotNull(jwt);

		JsonWebToken verifiedJsonWebToken = JsonWebToken.ofStringIgnoringExpiration(jwt, privateKey, publicKey);
		Assert.assertNotNull(verifiedJsonWebToken);
	}
}
