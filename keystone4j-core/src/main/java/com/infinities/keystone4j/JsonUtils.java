package com.infinities.keystone4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonUtils {

	private final static ObjectMapper objectMapper = new ObjectMapper();
	private final static TypeReference<HashMap<String, String>> mapTypeRef = new TypeReference<HashMap<String, String>>() {
	};
	static {
		AnnotationIntrospector primaryIntrospector = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondaryIntrospector = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pairIntrospector = new AnnotationIntrospector.Pair(primaryIntrospector, secondaryIntrospector);
		// make deserializer use JAXB annotations (only)
		objectMapper.getDeserializationConfig().withAnnotationIntrospector(pairIntrospector);
		// make serializer use JAXB annotations (only)
		objectMapper.getSerializationConfig().withAnnotationIntrospector(pairIntrospector);
	}


	public static String toJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		return objectMapper.writeValueAsString(object);
	}

	public static Map<String, String> readJson(File file) throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = objectMapper.readValue(file, mapTypeRef);
		return map;
	}

}
