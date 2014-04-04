package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.credential.Credential;

public class CredentialDao extends AbstractDao<Credential> {

	public CredentialDao() {
		super(Credential.class);
	}

	public List<Credential> listCredentialsForUser(String userid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Credential> cq = cb.createQuery(Credential.class);
		Root<Credential> root = cq.from(Credential.class);
		if (!Strings.isNullOrEmpty(userid)) {
			Predicate predicate = cb.equal(root.get("user").get("id"), userid);
			cq.where(predicate);
		}
		cq.select(root);
		TypedQuery<Credential> q = em.createQuery(cq);
		List<Credential> credentials = q.getResultList();
		// tx.commit();
		return credentials;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	public void removeCredentialForProject(String projectid) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("DELETE FROM Credential c WHERE c.project.id = :projectid ");
		query.setParameter("projectid", projectid);
		query.executeUpdate();
	}

	public void removeCredentialForUser(String userid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		Query query = em.createQuery("DELETE FROM Credential c WHERE c.user.id = :userid ");
		query.setParameter("userid", userid);
		query.executeUpdate();
		// tx.commit();
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}
}
