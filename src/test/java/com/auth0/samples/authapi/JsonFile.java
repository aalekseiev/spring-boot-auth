package com.auth0.samples.authapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;



public final class JsonFile {

	private final String fileName;
	
	public JsonFile(String fileName) {
		this.fileName = fileName;
	}
	
	public String content() throws IOException {
		InputStream inputStream = new ClassPathResource(fileName).getInputStream();
		StringWriter writer = new StringWriter();
		Charset encoding = Charset.forName("UTF-8");
		IOUtils.copy(inputStream, writer, encoding );
		return writer.toString();
	}
	
}
