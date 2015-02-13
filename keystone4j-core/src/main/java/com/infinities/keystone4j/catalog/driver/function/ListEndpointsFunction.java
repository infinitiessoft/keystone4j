package com.infinities.keystone4j.catalog.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.EndpointDao;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class ListEndpointsFunction implements ListFunction<Endpoint> {

	@Override
	public List<Endpoint> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new EndpointDao().listEndpoint(hints);
	}

}
