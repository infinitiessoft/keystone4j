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
