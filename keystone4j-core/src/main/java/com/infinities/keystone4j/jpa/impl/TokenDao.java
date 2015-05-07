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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.token.Token;

public class TokenDao extends AbstractDao<Token> {

	private final static Logger logger = LoggerFactory.getLogger(TokenDao.class);


	public TokenDao() {
		super(Token.class);
	}

	private boolean isTenantMatches(String tenantid, Token token) {
		return (Strings.isNullOrEmpty(tenantid) || (!Strings.isNullOrEmpty(token.getTenant().getId()) && token.getTenant()
				.getId().equals(tenantid)));
	}

	private boolean isConsumerMatches(String consumerid, Token token) {
		if (Strings.isNullOrEmpty(consumerid)) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteToken(String userid, String tenantid, String trustid, String consumerid) {
		Calendar now = Calendar.getInstance();
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, now);
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		if (!Strings.isNullOrEmpty(trustid)) {
			Predicate trustPredicate = cb.equal(root.get("trustId"), trustid);
			predicates.add(trustPredicate);
		} else {
			Predicate userPredicate = cb.equal(root.get("userId"), userid);
			predicates.add(userPredicate);
		}

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();

		for (Token tokenRef : tokens) {
			if (!Strings.isNullOrEmpty(tenantid)) {
				if (!isTenantMatches(tenantid, tokenRef)) {
					continue;
				}
			}

			if (!Strings.isNullOrEmpty(consumerid)) {
				if (!isConsumerMatches(consumerid, tokenRef)) {
					continue;
				}
			}

			tokenRef.setValid(false);
			em.merge(tokenRef);
		}
	}

	public List<Token> listRevokedTokens() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, Calendar.getInstance());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), false);
		predicates.add(validPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();
		return tokens;
	}

	public void flushExpiredTokens() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(getEntityType());
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.lessThan(path, Calendar.getInstance());
		predicates.add(expiresPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<Token> tokens = q.getResultList();
		int totalRemoved = 0;

		for (Token token : tokens) {
			em.remove(token);
			totalRemoved++;
		}
		logger.info("Total expired tokens removed: {}", totalRemoved);
	}

	public List<String> listTokensForTrust(String trustId) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, Calendar.getInstance());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		Predicate trustPredicate = cb.equal(root.get("trustId"), trustId);
		predicates.add(trustPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root.<String> get("id"));

		TypedQuery<String> q = em.createQuery(cq);
		List<String> tokens = q.getResultList();
		return tokens;
	}

	public List<String> listTokensForUser(String userId, String tenantId) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(Token.class);
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, Calendar.getInstance());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		Predicate trustPredicate = cb.equal(root.get("userId"), userId);
		predicates.add(trustPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<String> tokens = new ArrayList<String>();
		for (Token token : q.getResultList()) {
			if (isTenantMatches(tenantId, token)) {
				tokens.add(token.getId());
			}
		}
		return tokens;
	}

	public List<String> listTokensForConsumer(String userId, String consumerId) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Token> cq = cb.createQuery(Token.class);
		Root<Token> root = cq.from(getEntityType());
		Path<Calendar> path = root.get("expires");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate expiresPredicate = cb.greaterThan(path, Calendar.getInstance());
		predicates.add(expiresPredicate);

		Predicate validPredicate = cb.equal(root.get("valid"), true);
		predicates.add(validPredicate);

		Predicate trustPredicate = cb.equal(root.get("userId"), userId);
		predicates.add(trustPredicate);

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Token> q = em.createQuery(cq);
		List<String> tokens = new ArrayList<String>();
		for (Token token : q.getResultList()) {
			if (isConsumerMatches(consumerId, token)) {
				tokens.add(token.getId());
			}
		}
		return tokens;
	}
}
