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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.catalog.Region;

public class RegionDao extends AbstractDao<Region> {

	public RegionDao() {
		super(Region.class);
	}

	// public Domain findByName(String name) {
	// EntityManager em = getEntityManager();
	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// CriteriaQuery<Domain> cq = cb.createQuery(getEntityType());
	// Root<Domain> root = cq.from(getEntityType());
	// Path<String> path = root.get("name");
	// List<Predicate> predicates = Lists.newArrayList();
	// Predicate namePredicate = cb.equal(path, name);
	// predicates.add(namePredicate);
	// cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	// cq.select(root);
	// TypedQuery<Domain> q = em.createQuery(cq);
	// Domain domain = q.getSingleResult();
	// return domain;
	// }
	//
	// public List<Domain> listDomainsForUser(String userid, List<String>
	// groupids) {
	// EntityManager em = getEntityManager();
	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// CriteriaQuery<Domain> cq = cb.createQuery(Domain.class);
	// Root<Domain> root = cq.from(Domain.class);
	// Root<RoleAssignment> roleAssignmentRoot = cq.from(RoleAssignment.class);
	//
	// List<Predicate> predicates = Lists.newArrayList();
	// Predicate targetIdPredicate = cb.equal(root.get("id"),
	// roleAssignmentRoot.get("targetId"));
	// predicates.add(targetIdPredicate);
	//
	// List<Predicate> filters = Lists.newArrayList();
	// if (!Strings.isNullOrEmpty(userid)) {
	// filters.add(cb.and(cb.equal(roleAssignmentRoot.get("actorId"), userid),
	// cb.equal(roleAssignmentRoot.get("inherited"), false),
	// cb.equal(roleAssignmentRoot.get("type"), AssignmentType.USER_DOMAIN)));
	// }
	//
	// if (groupids != null) {
	// filters.add(cb.and(roleAssignmentRoot.get("actorId").in(groupids),
	// cb.equal(roleAssignmentRoot.get("inherited"), false),
	// cb.equal(roleAssignmentRoot.get("type"), AssignmentType.GROUP_DOMAIN)));
	// }
	//
	// if (filters.isEmpty()) {
	// return new ArrayList<Domain>();
	// }
	//
	// cq.select(root);
	// predicates.add(cb.or(filters.toArray(new Predicate[filters.size()])));
	// cq.where(predicates.toArray(new Predicate[predicates.size()]));
	// TypedQuery<Domain> q = em.createQuery(cq);
	// List<Domain> domains = q.getResultList();
	//
	// return domains;
	// }
	//
	// public List<Domain> listDomainsForGroups(List<String> groupids) {
	// EntityManager em = getEntityManager();
	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// CriteriaQuery<Domain> cq = cb.createQuery(Domain.class);
	// Root<Domain> root = cq.from(Domain.class);
	// Root<RoleAssignment> roleAssignmentRoot = cq.from(RoleAssignment.class);
	//
	// Predicate predicate = cb.and(cb.equal(root.get("id"),
	// roleAssignmentRoot.get("targetId")),
	// cb.isFalse(roleAssignmentRoot.<Boolean> get("inherited")),
	// cb.equal(roleAssignmentRoot.get("type"), AssignmentType.GROUP_DOMAIN),
	// roleAssignmentRoot.get("actorId").in(groupids));
	// cq.select(root);
	// cq.where(predicate);
	// TypedQuery<Domain> q = em.createQuery(cq);
	// List<Domain> domains = q.getResultList();
	//
	// return domains;
	// }

	public List<Region> listRegion(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Region.class, hints);
	}

	public List<Region> listByParentRegionId(String id) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Region> cq = cb.createQuery(Region.class);
		Root<Region> root = cq.from(Region.class);
		Predicate predicate = cb.and(cb.equal(root.get("parentRegionId"), id));
		cq.select(root);
		cq.where(predicate);
		TypedQuery<Region> q = em.createQuery(cq);
		List<Region> regions = q.getResultList();

		return regions;
	}
}
