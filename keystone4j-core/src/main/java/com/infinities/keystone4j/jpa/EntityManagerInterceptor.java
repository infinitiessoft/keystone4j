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
package com.infinities.keystone4j.jpa;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(1000)
public class EntityManagerInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

	private final Logger logger = LoggerFactory.getLogger(EntityManagerInterceptor.class);


	// private boolean closed = true;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("==========================================================request come");
		try {
			EntityManagerHelper.beginTransaction();
		} catch (RuntimeException e) {
			EntityManagerHelper.closeEntityManager();
			logger.debug("transaction is closed unexpected", e);
			requestContext.abortWith(Response.serverError().build());
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		System.out.println("==========================================================request leave");
		if (!EntityManagerHelper.getLock()) {
			try {
				EntityManagerHelper.commit();
				logger.debug("transaction is commit");
			} catch (RuntimeException e) {
				if (EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()
						&& EntityManagerHelper.getEntityManager().getTransaction().isActive()) {
					EntityManagerHelper.rollback();
					logger.debug("transaction is rollback", e);
				}
				throw e;
			} catch (Exception e) {
				logger.debug("transaction is closed", e);
				throw e;
			} finally {
				EntityManagerHelper.closeEntityManager();
			}
		}
	}

}
