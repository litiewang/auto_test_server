package com.auto.test.model.postman;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
public class PostmanReader {
	ObjectMapper om;
	
	public PostmanReader() {
		om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public PostmanCollection readCollectionFileClasspath(String fileOnClasspath) throws JsonParseException, JsonMappingException, IOException {
		String fileName = fileOnClasspath.substring(fileOnClasspath.indexOf(":")+1);
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		
		PostmanCollection collection = om.readValue(stream, PostmanCollection.class);
		stream.close();
		return collection;
	}
	
	public PostmanEnvironment readEnvironmentFileClasspath(String fileOnClasspath) throws JsonParseException, JsonMappingException, IOException {
		String fileName = fileOnClasspath.substring(fileOnClasspath.indexOf(":")+1);
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		
		PostmanEnvironment env = om.readValue(stream, PostmanEnvironment.class);
		stream.close();
		return env;
	}
	
	public PostmanCollection readCollectionFile(String filePath) throws IOException {
		if (filePath.startsWith("classpath:")) {
			return readCollectionFileClasspath(filePath);
		}
		InputStream stream = new FileInputStream(new File(filePath));
//		PostmanCollection collection = om.readValue(stream, PostmanCollection.class);
//		stream.close();
		return readCollectionFile(stream);
	}
	public PostmanCollection readCollectionFile(InputStream stream) throws IOException {
		
		PostmanCollection collection = om.readValue(stream, PostmanCollection.class);
		stream.close();
		return collection;
	}
	public PostmanEnvironment readEnvironmentFile(String filePath) throws IOException {
		if (filePath == null) {
			return new PostmanEnvironment();
		}
		if (filePath.startsWith("classpath:")) {
			return readEnvironmentFileClasspath(filePath);
		}
		InputStream stream = new FileInputStream(new File(filePath));
		PostmanEnvironment env = om.readValue(stream, PostmanEnvironment.class);
		stream.close();
		return env;
	}
}
