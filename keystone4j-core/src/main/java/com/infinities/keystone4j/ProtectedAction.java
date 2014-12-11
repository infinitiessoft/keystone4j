package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.model.MemberWrapper;

public interface ProtectedAction<T> extends Action {

	MemberWrapper<T> execute(ContainerRequestContext context) throws Exception;

}
