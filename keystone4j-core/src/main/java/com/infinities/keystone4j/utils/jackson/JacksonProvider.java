package com.infinities.keystone4j.utils.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class JacksonProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider {

	public JacksonProvider() {
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// if using BOTH JAXB annotations AND Jackson annotations:
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();

		ObjectMapper mapper = new ObjectMapper().registerModule(new Hibernate4Module())
				.setSerializationInclusion(Include.NON_NULL)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).enable(SerializationFeature.INDENT_OUTPUT)
				.setAnnotationIntrospector(new AnnotationIntrospectorPair(introspector, secondary));
		// mapper = mapper.setSerializationInclusion(Include)
		setMapper(mapper);
	}
}
