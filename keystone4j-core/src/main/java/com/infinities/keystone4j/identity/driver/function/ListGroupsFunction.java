package com.infinities.keystone4j.identity.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.GroupDao;
import com.infinities.keystone4j.model.identity.Group;

public class ListGroupsFunction implements ListFunction<Group> {

	@Override
	public List<Group> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new GroupDao().listGroups(hints);
	}

}
