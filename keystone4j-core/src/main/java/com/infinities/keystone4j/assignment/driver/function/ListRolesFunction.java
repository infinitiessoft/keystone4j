package com.infinities.keystone4j.assignment.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.RoleDao;
import com.infinities.keystone4j.model.assignment.Role;

public class ListRolesFunction implements ListFunction<Role> {

	@Override
	public List<Role> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new RoleDao().listRole(hints);
	}

}
