package com.infinities.keystone4j.endpointfilter.api.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;

public abstract class AbstractEndpointFilterCommand<T> implements Command<T> {

	private final EndpointFilterDriver endpointFilterDriver;


	public AbstractEndpointFilterCommand(EndpointFilterDriver endpointFilterDriver) {
		super();
		this.endpointFilterDriver = endpointFilterDriver;
	}

	public EndpointFilterDriver getEndpointFilterDriver() {
		return endpointFilterDriver;
	}
}
