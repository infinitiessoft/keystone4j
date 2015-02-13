package com.infinities.keystone4j.catalog.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.ServiceDao;
import com.infinities.keystone4j.model.catalog.Service;

public class ListServicesFunction implements ListFunction<Service> {

	@Override
	public List<Service> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new ServiceDao().listService(hints);
	}

}
