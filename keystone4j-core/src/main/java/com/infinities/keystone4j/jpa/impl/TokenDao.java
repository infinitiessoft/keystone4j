package com.infinities.keystone4j.jpa.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.token.model.Token;

public class TokenDao extends AbstractDao<Token> {

	public TokenDao() {
		super(Token.class);
	}

	public List<Token> findBeforeExpireAndValid(Date date, boolean valid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Date> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, date);
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), valid);
		predicates.add(validPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = getEntityManager().createQuery(cq);
		List<Token> tokens = q.getResultList();
		return tokens;
	}

	public void deleteTokensForTrust(String userid, String projectid, String trustid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Date> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, new Date());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		Predicate trustPredicate = cb.equal(root.get("trust").get("id"), trustid);
		predicates.add(trustPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = getEntityManager().createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			if (!Strings.isNullOrEmpty(projectid)) {
				if (!isTenantMatches(projectid, token)) {
					continue;
				}
			}

			token.setValid(false);
			this.merge(token);
		}
	}

	public void deleteTokensForUser(String userid, String projectid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Date> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, new Date());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		Predicate userPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(userPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = getEntityManager().createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			if (!Strings.isNullOrEmpty(projectid)) {
				if (!isTenantMatches(projectid, token)) {
					continue;
				}
			}

			token.setValid(false);
			this.merge(token);
		}
	}

	public void flushExpiredTokens() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Date> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.lessThan(path, new Date());
		predicates.add(expiresPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = getEntityManager().createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			this.remove(token);
		}
	}

	private boolean isTenantMatches(String tenantid, Token token) {
		return (Strings.isNullOrEmpty(tenantid) || (token.getTrust().getProject() != null && token.getTrust().getProject()
				.getId().equals(tenantid)));
	}
}
