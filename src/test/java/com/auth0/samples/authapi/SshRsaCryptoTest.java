package com.auth0.samples.authapi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthorizationServerApplication.class})
public class SshRsaCryptoTest {

	@Value(value = "${jwt.private-key}")
	private String privateKey;

	@Value(value = "${jwt.public-key}")
	private String publicKey;
	@Test
	public void shouldReadPrivateKey() throws GeneralSecurityException, IOException {
		Assert.assertNotNull(privateKey);
		SshRsaCrypto sshRsaCrypto = new SshRsaCrypto();
		PrivateKey privateKey = sshRsaCrypto.readPrivateKey(sshRsaCrypto.slurpPrivateKey(this.privateKey));

		Assert.assertNotNull(privateKey);
	}

	@Test
	public void shouldReadFullPublicKey() throws IOException, GeneralSecurityException {
		String fullPublicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDCUcNvA97+eB/om/MrtRTr4CH47oRTbFlMyr7HH/mmJdc3+s7FUoxqgtMlWKbXjrT6/fLRyGZGnHiSXuqN2ZbI3DMj5TicZJb1M8bRJa1Qfb+fFLhbOwjkymrR+C/pKIHBFoLe81VYlqZ+VKNADYaC3A268s++tz/7Dc1xbxKaRmfxUQ4LiDDCi6fi8j4w4IdytMFsYKDP1YYkhQMNsSH1v0vPxlMbIj7HB6h6cK87/hZku5PTLfl3gaHYfGwAoEFUtbOWZT1AqTKqZcDuTmYAn7WWufz7bHbbpf73xxebOg4eej8suuYBZZP8QsB05AMWGONgnkSlG/QqnL8Qwfpt name@hostname";
		SshRsaCrypto sshRsaCrypto = new SshRsaCrypto();
		PublicKey publicKey = sshRsaCrypto.readPublicKey(sshRsaCrypto.slurpPublicKey(fullPublicKey));
		Assert.assertNotNull(publicKey);
	}

	@Test
	public void shouldReadShortPublicKey() throws IOException, GeneralSecurityException {
		String fullPublicKey = "AAAAB3NzaC1yc2EAAAADAQABAAABAQDCUcNvA97+eB/om/MrtRTr4CH47oRTbFlMyr7HH/mmJdc3+s7FUoxqgtMlWKbXjrT6/fLRyGZGnHiSXuqN2ZbI3DMj5TicZJb1M8bRJa1Qfb+fFLhbOwjkymrR+C/pKIHBFoLe81VYlqZ+VKNADYaC3A268s++tz/7Dc1xbxKaRmfxUQ4LiDDCi6fi8j4w4IdytMFsYKDP1YYkhQMNsSH1v0vPxlMbIj7HB6h6cK87/hZku5PTLfl3gaHYfGwAoEFUtbOWZT1AqTKqZcDuTmYAn7WWufz7bHbbpf73xxebOg4eej8suuYBZZP8QsB05AMWGONgnkSlG/QqnL8Qwfpt";
		SshRsaCrypto sshRsaCrypto = new SshRsaCrypto();
		PublicKey publicKey = sshRsaCrypto.readPublicKey(sshRsaCrypto.slurpPublicKey(fullPublicKey));
		Assert.assertNotNull(publicKey);
	}
}
