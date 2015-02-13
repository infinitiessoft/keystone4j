package com.infinities.keystone4j.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignment.AssignmentType;

public class RoleDao extends AbstractDao<Role> {

	public RoleDao() {
		super(Role.class);
	}

	@Override
	public void remove(Role persistentInstance) {
		EntityManager em = getEntityManager();

		// Set<TokenRole> groupDomainGrantMetadatas =
		// persistentInstance.getTokenRoles();
		// for (TokenRole groupDomainGrantMetadata : groupDomainGrantMetadatas)
		// {
		// em.remove(em.merge(groupDomainGrantMetadata));
		// persistentInstance.getGroupDomainGrantMetadatas().remove(groupDomainGrantMetadata);
		//
		// em.flush();
		// }
		// persistentInstance.getGroupDomainGrantMetadatas().clear();

		em.remove(persistentInstance);
	}

	public List<Role> listGrants(String actorId, String targetId, boolean inheritedToProjects) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(Role.class);
		Root<Role> root = cq.from(Role.class);
		Join<Role, RoleAssignment> roleAssignment = root.join("roleAssignments");

		List<Predicate> predicates = Lists.newArrayList();
		Predicate actorIdPredicate = cb.equal(roleAssignment.get("actorId"), actorId);
		predicates.add(actorIdPredicate);
		Predicate targetIdPredicate = cb.equal(roleAssignment.get("targetId"), targetId);
		predicates.add(targetIdPredicate);
		Predicate inheritedPredicate = cb.equal(roleAssignment.get("inherited"), inheritedToProjects);
		predicates.add(inheritedPredicate);
		cq.select(root);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		TypedQuery<Role> q = em.createQuery(cq);
		List<Role> roles = q.getResultList();

		return roles;

	}

	public List<Role> getRolesForGroupsOnDomain(List<String> groupids, String domainid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(Role.class);
		Root<Role> root = cq.from(Role.class);
		Join<Role, RoleAssignment> roleAssignment = root.join("roleAssignments");

		List<Predicate> predicates = Lists.newArrayList();
		Predicate actorIdPredicate = roleAssignment.get("actorId").in(groupids);
		predicates.add(actorIdPredicate);
		Predicate targetIdPredicate = cb.equal(roleAssignment.get("targetId"), domainid);
		predicates.add(targetIdPredicate);
		Predicate typePredicate = cb.equal(roleAssignment.get("type"), AssignmentType.GROUP_DOMAIN);
		predicates.add(typePredicate);
		Predicate inheritedPredicate = cb.isFalse(roleAssignment.<Boolean> get("inherited"));
		predicates.add(inheritedPredicate);
		cq.select(root);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		TypedQuery<Role> q = em.createQuery(cq);
		List<Role> roles = q.getResultList();

		return roles;
	}

	public List<Role> listRolesInIds(List<String> roleids) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(getEntityType());
		Root<Role> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate idPredicate = root.get("id").in(roleids);
		predicates.add(idPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Role> q = em.createQuery(cq);
		List<Role> projects = q.getResultList();
		return projects;
	}

	public List<Role> listRole(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Role.class, hints);
	}
}
