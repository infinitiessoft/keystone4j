/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.utils.jackson;

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