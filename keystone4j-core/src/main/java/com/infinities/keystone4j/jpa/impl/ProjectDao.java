package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.jpa.AbstractDao;

public class ProjectDao extends AbstractDao<Project> {

	public ProjectDao() {
		super(Project.class);
	}

	public Project findByName(String name, String domainid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
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

		TypedQuery<Project> q = getEntityManager().createQuery(cq);
		Project project = q.getSingleResult();
		return project;
	}

	public List<Project> listProject(String domainid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Project> cq = cb.createQuery(getEntityType());
		Root<Project> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
			predicates.add(domainidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Project> q = getEntityManager().createQuery(cq);
		List<Project> projects = q.getResultList();
		return projects;
	}
}
