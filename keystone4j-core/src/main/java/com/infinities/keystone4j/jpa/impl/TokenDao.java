package com.infinities.keystone4j.jpa.impl;

import java.util.Date;
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
import com.infinities.keystone4j.token.model.Token;

public class TokenDao extends AbstractDao<Token> {

	public TokenDao() {
		super(Token.class);
	}

	public List<Token> findBeforeExpireAndValid(Date date, boolean valid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
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

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();

		// tx.commit();
		return tokens;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public void deleteTokensForTrust(String userid, String trustid) {

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
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

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			if (!Strings.isNullOrEmpty(trustid)) {
				if (!isTrustMatches(trustid, token)) {
					continue;
				}
			}

			token.setValid(false);
			em.merge(token);
		}

	}

	public void deleteTokensForUser(String userid, String projectid) {

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
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

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			if (!Strings.isNullOrEmpty(projectid)) {
				if (!isTenantMatches(projectid, token)) {
					continue;
				}
			}

			token.setValid(false);
			em.merge(token);
		}

	}

	public void flushExpiredTokens() {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Date> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.lessThan(path, new Date());
		predicates.add(expiresPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token token : tokens) {
			em.remove(token);
		}
		// tx.commit();
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	private boolean isTrustMatches(String trustid, Token token) {
		return (Strings.isNullOrEmpty(trustid) || (token.getTrust() != null && token.getTrust().getId().equals(trustid)));
	}

	private boolean isTenantMatches(String tenantid, Token token) {
		return (Strings.isNullOrEmpty(tenantid) || (token.getProject() != null && token.getProject().getId()
				.equals(tenantid)));
	}
}
