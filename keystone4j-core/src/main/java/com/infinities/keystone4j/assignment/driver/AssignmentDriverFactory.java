package com.infinities.keystone4j.assignment.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.common.Config;

public class AssignmentDriverFactory implements Factory<AssignmentDriver> {

	public AssignmentDriverFactory() {
	}

	@Override
	public void dispose(AssignmentDriver arg0) {

	}

	@Override
	public AssignmentDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.assignment, "driver").getText();
		try {
			Class<?> c = Class.forName(driver);
			return (AssignmentDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
