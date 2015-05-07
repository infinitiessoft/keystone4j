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
//package com.infinities.keystone4j.jpa.impl;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import com.google.common.base.Strings;
//import com.google.common.collect.Lists;
//import com.infinities.keystone4j.jpa.AbstractDao;
//import com.infinities.keystone4j.model.endpointfilter.ProjectEndpoint;
//
//public class ProjectEndpointDao extends AbstractDao<ProjectEndpoint> {
//
//	public ProjectEndpointDao() {
//		super(ProjectEndpoint.class);
//	}
//
//	public ProjectEndpoint findByProjectidAndEndpointid(String projectid, String endpointid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
//		Root<ProjectEndpoint> root = cq.from(getEntityType());
//		List<Predicate> predicates = Lists.newArrayList();
//		Predicate projectidPredicate = cb.equal(root.get("project").get("id"), projectid);
//		predicates.add(projectidPredicate);
//		Predicate endpointidPredicate = cb.equal(root.get("endpoint").get("id"), endpointid);
//		predicates.add(endpointidPredicate);
//		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		cq.select(root);
//
//		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
//		ProjectEndpoint projectEndpoint = q.getSingleResult();
//		// tx.commit();
//		return projectEndpoint;
//
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
//
//	public List<ProjectEndpoint> listEndpointsForProject(String projectid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
//		Root<ProjectEndpoint> root = cq.from(getEntityType());
//		List<Predicate> predicates = Lists.newArrayList();
//		if (!Strings.isNullOrEmpty(projectid)) {
//			Predicate projectidPredicate = cb.equal(root.get("project").get("id"), projectid);
//			predicates.add(projectidPredicate);
//		}
//		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		cq.select(root);
//
//		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
//		List<ProjectEndpoint> projectEndpoints = q.getResultList();
//		// tx.commit();
//		return projectEndpoints;
//
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
//
//	public List<ProjectEndpoint> listProjectsForEndpoint(String endpointid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ProjectEndpoint> cq = cb.createQuery(getEntityType());
//		Root<ProjectEndpoint> root = cq.from(getEntityType());
//		List<Predicate> predicates = Lists.newArrayList();
//		if (!Strings.isNullOrEmpty(endpointid)) {
//			Predicate endpointidPredicate = cb.equal(root.get("endpoint").get("id"), endpointid);
//			predicates.add(endpointidPredicate);
//		}
//		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		cq.select(root);
//
//		TypedQuery<ProjectEndpoint> q = em.createQuery(cq);
//		List<ProjectEndpoint> projectEndpoints = q.getResultList();
//		// tx.commit();
//		return projectEndpoints;
//
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
// }
