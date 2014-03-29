package com.infinities.keystone4j;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.CommonProperties;

import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;

public class JacksonFeature implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		final String disableMoxy = CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.'
				+ context.getConfiguration().getRuntimeType().name().toLowerCase();
		context.property(disableMoxy, true);

		// add the default Jackson exception mappers
		context.register(JsonParseExceptionMapper.class);
		context.register(JsonMappingExceptionMapper.class);
		context.register(JacksonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
		return true;
	}
}