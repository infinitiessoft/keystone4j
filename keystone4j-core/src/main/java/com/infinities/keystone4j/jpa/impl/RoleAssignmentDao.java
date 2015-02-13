package com.infinities.keystone4j.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignment.AssignmentType;

public class RoleAssignmentDao extends AbstractDao<RoleAssignment> {

	public RoleAssignmentDao() {
		super(RoleAssignment.class);
	}

	public List<String> listUserIdsForProject(String projectid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		Path<RoleAssignment.AssignmentType> path = root.get("type");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate typePredicate = cb.equal(path, RoleAssignment.AssignmentType.USER_PROJECT);
		predicates.add(typePredicate);
		Predicate targetPredicate = cb.equal(root.get("targetId"), projectid);
		predicates.add(targetPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root.<String> get("actorId"));
		cq.distinct(true);
		TypedQuery<String> q = em.createQuery(cq);
		List<String> actorIds = q.getResultList();
		return actorIds;
	}

	public RoleAssignment getGrant(String roleid, String targetid, String actorid, boolean inheritedToProjects) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleAssignment> cq = cb.createQuery(RoleAssignment.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate actorPredicate = cb.equal(root.get("actorId"), actorid);
		predicates.add(actorPredicate);
		Predicate targetPredicate = cb.equal(root.get("targetId"), targetid);
		predicates.add(targetPredicate);
		Predicate rolePredicate = cb.equal(root.get("role").get("id"), roleid);
		predicates.add(rolePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<RoleAssignment> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	public List<RoleAssignment> listRoleAssignmentsForActors(List<String> actorList) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleAssignment> cq = cb.createQuery(RoleAssignment.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate actorPredicate = root.get("actorId").in(actorList);
		predicates.add(actorPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<RoleAssignment> q = em.createQuery(cq);
		return q.getResultList();
	}

	public List<String> getGroupProjectRoles(List<String> groups, String projectid, String projectDomainId,
			AssignmentDriver driver) throws Exception {
		if (groups.isEmpty()) {
			return new ArrayList<String>();
		}

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		Predicate predicate1 = cb.and(cb.equal(root.get("type"), AssignmentType.GROUP_PROJECT),
				cb.isFalse(root.<Boolean> get("inherited")), cb.equal(root.get("targetId"), projectid));
		if (Config.Instance.getOpt(Type.os_inherit, "enabled").asBoolean()) {
			Predicate predicate2 = cb.and(cb.equal(root.get("type"), AssignmentType.GROUP_DOMAIN),
					cb.isTrue(root.<Boolean> get("inherited")), cb.equal(root.get("targetId"), projectDomainId));
			predicate1 = cb.or(predicate1, predicate2);

			List<String> projectParents = new ArrayList<String>();
			for (Project project : driver.listProjectParents(projectid)) {
				projectParents.add(project.getId());
			}
			if (!projectParents.isEmpty()) {
				Predicate predicate3 = cb.and(cb.equal(root.get("type"), AssignmentType.GROUP_PROJECT),
						cb.isTrue(root.<Boolean> get("inherited")), root.get("targetId").in(projectDomainId));
				predicate1 = cb.or(predicate1, predicate3);
			}
		}

		predicate1 = cb.and(predicate1, root.get("actorId").in(groups));

		cq.where(predicate1);
		cq.select(root.<String> get("role").<String> get("id")).distinct(true);
		TypedQuery<String> q = em.createQuery(cq);
		List<String> roleIds = q.getResultList();

		return roleIds;
	}

	public void removeByRoleId(String roleid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleAssignment> cq = cb.createQuery(RoleAssignment.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate rolePredicate = cb.equal(root.get("role").get("id"), roleid);
		predicates.add(rolePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<RoleAssignment> q = em.createQuery(cq);
		List<RoleAssignment> roleAssignments = q.getResultList();
		for (RoleAssignment ref : roleAssignments) {
			remove(ref);
		}
	}

	public void removeByActorId(String actorid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleAssignment> cq = cb.createQuery(RoleAssignment.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate rolePredicate = cb.equal(root.get("actorId"), actorid);
		predicates.add(rolePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<RoleAssignment> q = em.createQuery(cq);
		List<RoleAssignment> roleAssignments = q.getResultList();
		for (RoleAssignment ref : roleAssignments) {
			remove(ref);
		}
	}

	public List<RoleAssignment> findAll(AssignmentType type, String actorId, String targetId) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleAssignment> cq = cb.createQuery(RoleAssignment.class);
		Root<RoleAssignment> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate actorPredicate = cb.equal(root.get("actorId"), actorId);
		predicates.add(actorPredicate);
		Predicate targetPredicate = cb.equal(root.get("targetId"), targetId);
		predicates.add(targetPredicate);
		Predicate typePredicate = cb.equal(root.get("type"), type);
		predicates.add(typePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<RoleAssignment> q = em.createQuery(cq);
		return q.getResultList();
	}
}
