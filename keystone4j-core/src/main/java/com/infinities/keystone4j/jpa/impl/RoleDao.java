package com.infinities.keystone4j.jpa.impl;

import javax.persistence.EntityManager;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.Role;

public class RoleDao extends AbstractDao<Role> {

	public RoleDao() {
		super(Role.class);
	}

	@Override
	public void remove(Role persistentInstance) {
		EntityManager em = getEntityManager();

		// Set<TokenRole> groupDomainGrantMetadatas =
		// persistentInstance.getTokenRoles();
		// for (TokenRole groupDomainGrantMetadata : groupDomainGrantMetadatas)
		// {
		// em.remove(em.merge(groupDomainGrantMetadata));
		// persistentInstance.getGroupDomainGrantMetadatas().remove(groupDomainGrantMetadata);
		//
		// em.flush();
		// }
		// persistentInstance.getGroupDomainGrantMetadatas().clear();

		em.remove(persistentInstance);
	}

}
