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

package com.infinities.keystone4j;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(1000)
public class CORSResponseFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(CORSResponseFilter.class);


	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		logger.debug("filter cors");
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		headers.add("Access-Control-Allow-Origin", "*");
		// headers.add("Access-Control-Allow-Origin",
		// "http://podcastpedia.org"); //allows CORS requests only coming from
		// podcastpedia.org
		headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH");
		headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, ,X-Subject-Token, X-Auth-Token");
		headers.add("Access-Control-Expose-Headers", "X-Requested-With, Content-Type, ,X-Subject-Token, X-Auth-Token");
	}
}
