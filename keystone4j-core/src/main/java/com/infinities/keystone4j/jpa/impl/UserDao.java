package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.UserProjectGrant;
import com.infinities.keystone4j.model.identity.User;

public class UserDao extends AbstractDao<User> {

	public UserDao() {
		super(User.class);
	}

	public List<User> listUsersForProject(String projectid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		Join<User, UserProjectGrant> grant = root.join("userProjectGrants");
		cq.select(root).where(cb.equal(grant.get("project").get("id"), projectid));
		TypedQuery<User> q = em.createQuery(cq);
		List<User> users = q.getResultList();

		return users;

	}

	public User findByName(String name, String domainid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(getEntityType());
		Root<User> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		Predicate domainPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(domainPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<User> q = em.createQuery(cq);
		User user = q.getSingleResult();
		// tx.commit();
		return user;

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
