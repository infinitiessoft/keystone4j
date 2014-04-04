package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.Project;

public class ProjectDao extends AbstractDao<Project> {

	public ProjectDao() {
		super(Project.class);
	}

	public Project findByName(String name, String domainid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
			predicates.add(domainidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		Project project = q.getSingleResult();

		// tx.commit();
		return project;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<Project> listProject(String domainid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
			predicates.add(domainidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		List<Project> projects = q.getResultList();
		// tx.commit();
		return projects;

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
