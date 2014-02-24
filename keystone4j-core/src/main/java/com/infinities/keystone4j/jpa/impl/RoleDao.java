package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.jpa.AbstractDao;

public class RoleDao extends AbstractDao<Role> {

	public RoleDao() {
		super(Role.class);
	}

}
