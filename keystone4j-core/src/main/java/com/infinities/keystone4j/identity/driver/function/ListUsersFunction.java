package com.infinities.keystone4j.identity.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.UserDao;
import com.infinities.keystone4j.model.identity.User;

public class ListUsersFunction implements ListFunction<User> {

	@Override
	public List<User> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new UserDao().listUsers(hints);
	}

}
