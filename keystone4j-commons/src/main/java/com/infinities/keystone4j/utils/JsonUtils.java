package com.infinities.keystone4j.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();
	private final static TypeReference<LinkedHashMap<String, String>> mapTypeRef = new TypeReference<LinkedHashMap<String, String>>() {
	};

	static {
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// if using BOTH JAXB annotations AND Jackson annotations:
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
		objectMapper = objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(introspector, secondary));
		objectMapper = objectMapper.setSerializationInclusion(Include.NON_NULL);
		// make deserializer use JAXB annotations (only)
		objectMapper = objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		// objectMapper =
		// objectMapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
		// objectMapper =
		// objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
		// false);
		objectMapper.registerModule(new Hibernate4Module());
	}


	public static String toJsonWithoutPrettyPrint(Object object) throws JsonGenerationException, JsonMappingException,
			IOException {
		try {
			objectMapper = objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
			return objectMapper.writeValueAsString(object);
		} finally {
			objectMapper = objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
	}

	public static String toJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		return objectMapper.writeValueAsString(object);
	}

	public static String toJson(Object object, Class<?> view) throws JsonGenerationException, JsonMappingException,
			IOException {
		return objectMapper.writerWithView(view).writeValueAsString(object);
	}

	public static Map<String, String> readJson(URL url) throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = objectMapper.readValue(url, mapTypeRef);
		return map;
	}

	public static <T> T readJson(String fromValue, TypeReference<T> toValueType) throws IOException {
		return objectMapper.readValue(fromValue, toValueType);
	}

	public static JsonNode convertToJsonNode(String text) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); // since 2.1 use
													// mapper.getFactory()
													// instead
		JsonParser jp = factory.createParser(text);
		JsonNode node = mapper.readTree(jp);
		return node;
	}

	public static <T> T readFile(String fileName, Class<T> valueType) throws JsonParseException, JsonMappingException,
			IOException {
		return objectMapper.readValue(new File(fileName), valueType);
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) throws JsonParseException,
			JsonMappingException, IOException {
		return objectMapper.convertValue(fromValue, toValueType);
	}

}
