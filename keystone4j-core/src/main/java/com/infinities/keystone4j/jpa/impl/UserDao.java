package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.jpa.AbstractDao;

public class UserDao extends AbstractDao<User> {

	public UserDao() {
		super(User.class);
	}

	public List<User> listUsersForProject(String projectid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		Predicate predicate = cb.equal(root.get("project").get("id"), projectid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserProjectGrant> q = getEntityManager().createQuery(cq);
		List<UserProjectGrant> grants = q.getResultList();

		List<User> users = Lists.newArrayList();
		for (UserProjectGrant grant : grants) {
			users.add(grant.getUser());
		}

		return users;
	}

	public User findByName(String name, String domainid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
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

		TypedQuery<User> q = getEntityManager().createQuery(cq);
		User user = q.getSingleResult();
		return user;
	}
}
