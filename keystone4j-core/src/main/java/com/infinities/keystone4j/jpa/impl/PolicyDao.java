package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.policy.model.Policy;

public class PolicyDao extends AbstractDao<Policy> {

	public PolicyDao() {
		super(Policy.class);
	}

}
