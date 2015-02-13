package com.infinities.keystone4j.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.assignment.Project;

public class ProjectDao extends AbstractDao<Project> {

	public ProjectDao() {
		super(Project.class);
	}

	public Project findByName(String name, String domainid) {

		EntityManager em = getEntityManager();
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
		return project;
	}

	public List<Project> listProjectsInDomain(String domainid) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(domainidPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		List<Project> projects = q.getResultList();
		return projects;
	}

	public List<Project> listProject(Hints hints) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Project.class, hints);
	}

	public List<Project> listProjectsInDomains(Set<String> domainIds) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainidPredicate = root.get("domain").get("id").in(domainIds);
		predicates.add(domainidPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		List<Project> projects = q.getResultList();
		return projects;
	}

	public List<Project> listProjectsInProjectIds(Set<String> ids) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate idPredicate = root.get("id").in(ids);
		predicates.add(idPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		List<Project> projects = q.getResultList();
		return projects;
	}

	public List<Project> getChildren(List<String> projectIds) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate idPredicate = root.get("parent").get("id").in(projectIds);
		predicates.add(idPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = em.createQuery(cq);
		List<Project> projects = q.getResultList();
		return projects;
	}
}
