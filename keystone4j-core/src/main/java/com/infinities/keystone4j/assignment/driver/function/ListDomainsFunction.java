package com.infinities.keystone4j.assignment.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.DomainDao;
import com.infinities.keystone4j.model.assignment.Domain;

public class ListDomainsFunction implements ListFunction<Domain> {

	@Override
	public List<Domain> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new DomainDao().listDomain(hints);
	}

}
