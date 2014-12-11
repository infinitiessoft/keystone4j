package com.infinities.keystone4j;

public interface Action {

	// T execute(ContainerRequestContext context, String... filters) throws
	// Exception;

	String getName();

	String getCollectionName();

	String getMemberName();

}
