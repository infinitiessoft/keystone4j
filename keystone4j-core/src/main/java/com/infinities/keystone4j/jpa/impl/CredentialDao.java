/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.credential.Credential;

public class CredentialDao extends AbstractDao<Credential> {

	public CredentialDao() {
		super(Credential.class);
	}

	public List<Credential> listCredential(Hints hints) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Credential.class, hints);
	}

	public List<Credential> listCredentialsForUser(String userid) {

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Credential> cq = cb.createQuery(Credential.class);
		Root<Credential> root = cq.from(Credential.class);
		Predicate predicate = cb.equal(root.get("userId"), userid);
		cq.where(predicate);
		cq.select(root);
		TypedQuery<Credential> q = em.createQuery(cq);
		List<Credential> credentials = q.getResultList();
		return credentials;
	}

	public void removeCredentialForProject(String projectid) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("DELETE FROM Credential c WHERE c.projectId = :projectid ");
		query.setParameter("projectid", projectid);
		query.executeUpdate();
	}

	public void removeCredentialForUser(String userid) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("DELETE FROM Credential c WHERE c.userId = :userid ");
		query.setParameter("userid", userid);
		query.executeUpdate();
	}
}
