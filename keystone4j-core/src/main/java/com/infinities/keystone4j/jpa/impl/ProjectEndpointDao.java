package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.endpointfilter.ProjectEndpoint;

public class ProjectEndpointDao extends AbstractDao<ProjectEndpoint> {

	public ProjectEndpointDao() {
		super(ProjectEndpoint.class);
	}

	public ProjectEndpoint findByProjectidAndEndpointid(String projectid, String endpointid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
		Root<ProjectEndpoint> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate projectidPredicate = cb.equal(root.get("project").get("id"), projectid);
		predicates.add(projectidPredicate);
		Predicate endpointidPredicate = cb.equal(root.get("endpoint").get("id"), endpointid);
		predicates.add(endpointidPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
		ProjectEndpoint projectEndpoint = q.getSingleResult();
		// tx.commit();
		return projectEndpoint;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<ProjectEndpoint> listEndpointsForProject(String projectid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
		Root<ProjectEndpoint> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(projectid)) {
			Predicate projectidPredicate = cb.equal(root.get("project").get("id"), projectid);
			predicates.add(projectidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
		List<ProjectEndpoint> projectEndpoints = q.getResultList();
		// tx.commit();
		return projectEndpoints;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<ProjectEndpoint> listProjectsForEndpoint(String endpointid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
		Root<ProjectEndpoint> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(endpointid)) {
			Predicate endpointidPredicate = cb.equal(root.get("endpoint").get("id"), endpointid);
			predicates.add(endpointidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
		List<ProjectEndpoint> projectEndpoints = q.getResultList();
		// tx.commit();
		return projectEndpoints;

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
