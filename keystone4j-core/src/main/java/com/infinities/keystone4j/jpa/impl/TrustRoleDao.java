package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.trust.model.TrustRole;

public class TrustRoleDao extends AbstractDao<TrustRole> {

	public TrustRoleDao() {
		super(TrustRole.class);
	}
}
