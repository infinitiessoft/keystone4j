package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.jpa.AbstractDao;

public class CredentialDao extends AbstractDao<Credential> {

	public CredentialDao() {
		super(Credential.class);
	}

	public List<Credential> listCredentialsForUser(String userid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Credential> cq = cb.createQuery(Credential.class);
		Root<Credential> root = cq.from(Credential.class);
		if (!Strings.isNullOrEmpty(userid)) {
			Predicate predicate = cb.equal(root.get("user").get("id"), userid);
			cq.where(predicate);
		}
		cq.select(root);
		TypedQuery<Credential> q = getEntityManager().createQuery(cq);
		List<Credential> credentials = q.getResultList();
		return credentials;
	}

	public void removeCredentialForProject(String projectid) {
		Query query = getEntityManager().createQuery("DELETE FROM CREDENTIAL c where c.project.id = :projectid ");
		query.setParameter("projectid", projectid);
		query.executeUpdate();
	}

	public void removeCredentialForUser(String userid) {
		Query query = getEntityManager().createQuery("DELETE FROM CREDENTIAL c where c.user.id = :userid ");
		query.setParameter("userid", userid);
		query.executeUpdate();
	}
}
