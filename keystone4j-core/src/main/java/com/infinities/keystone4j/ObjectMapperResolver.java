package com.infinities.keystone4j;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

	private ObjectMapper objectMapper;


	public ObjectMapperResolver() {
		objectMapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// if using BOTH JAXB annotations AND Jackson annotations:
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
		objectMapper = objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(introspector, secondary));
		// make deserializer use JAXB annotations (only)
		objectMapper = objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		// objectMapper =
		// objectMapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
		// objectMapper =
		// objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
		// false);
		objectMapper.registerModule(new Hibernate4Module());

		// mapper = mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
