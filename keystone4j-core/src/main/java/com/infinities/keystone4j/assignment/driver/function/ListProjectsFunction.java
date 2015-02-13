package com.infinities.keystone4j.assignment.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.ProjectDao;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectsFunction implements ListFunction<Project> {

	@Override
	public List<Project> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new ProjectDao().listProject(hints);
	}

}
