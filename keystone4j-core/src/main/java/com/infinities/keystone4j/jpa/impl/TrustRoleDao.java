package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.trust.TrustRole;

public class TrustRoleDao extends AbstractDao<TrustRole> {

	public TrustRoleDao() {
		super(TrustRole.class);
	}
}
