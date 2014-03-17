package com.infinities.keystone4j;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper;


	public ObjectMapperResolver() {
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
		mapper = new ObjectMapper();
		mapper = mapper.enable(Feature.INDENT_OUTPUT);
		// mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION,
		// false);
		mapper = mapper.setSerializationInclusion(Inclusion.NON_NULL);
		mapper.setAnnotationIntrospector(pair);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
