package com.infinities.keystone4j.jpa;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class EntityManagerInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			EntityManagerHelper.beginTransaction();
		} catch (RuntimeException e) {
			if (EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()) {
				EntityManagerHelper.rollback();
			}
			EntityManagerHelper.closeEntityManager();
			throw e;
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		try {
			EntityManagerHelper.commit();
		} catch (RuntimeException e) {
			if (EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()) {
				EntityManagerHelper.rollback();
			}
			throw e;
		} finally {
			EntityManagerHelper.closeEntityManager();
		}

	}

}
