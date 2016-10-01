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
package com.infinities.keystone4j.jpa;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.Hints.Filter;
import com.infinities.keystone4j.utils.ReflectUtils;

public abstract class AbstractDao<T> implements GenericDao<T> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	// private EntityManager entityManager;
	private final Class<T> entityType;


	public AbstractDao(Class<T> entityType) {
		this.entityType = entityType;
	}

	// public void setEntityManager(EntityManager entityManager) {
	// this.entityManager = entityManager;
	// }

	// protected EntityManager getEntityManager() {
	// return entityManager;
	// }

	protected EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	@Override
	public void persist(T transientInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		em.persist(transientInstance);
		// tx.commit();
		logger.debug("persisting {} instance", getEntityType().getSimpleName());
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public void remove(T persistentInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		em.remove(em.merge(persistentInstance));
		// tx.commit();
		// logger.debug("removing {} instance",
		// getEntityType().getSimpleName());
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public T merge(T detachedInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		T result = em.merge(detachedInstance);
		// tx.commit();
		logger.debug("merging {} instance", getEntityType().getSimpleName());
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	@Override
	public T findById(Object id) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		T result = em.find(getEntityType(), id);
		// tx.commit();
		logger.debug("getting {} instance with id: {}", new Object[] { getEntityType().getSimpleName(), id });
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public List<T> findAll() {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityType());
		Root<T> taskEventRoot = cq.from(getEntityType());
		cq.select(taskEventRoot);
		TypedQuery<T> q = em.createQuery(cq);
		List<T> result = q.getResultList();
		// tx.commit();
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	protected <S> List<S> filterLimitQuery(Class<S> model, Hints hints) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<S> cq = cb.createQuery(model);
		Root<S> root = cq.from(model);
		cq.select(root);
		return filterLimitQuery(model, em, cq, cb, root, hints);
	}

	private <S> List<S> filterLimitQuery(Class<S> model, EntityManager em, CriteriaQuery<S> query, CriteriaBuilder cb,
			Root<S> root, Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		if (hints == null) {
			TypedQuery<S> q = em.createQuery(query);
			return q.getResultList();
		}
		query = filter(model, query, cb, root, hints);
		if (hints.getFilters().isEmpty()) {
			return limit(em, query, hints);
		} else {
			TypedQuery<S> q = em.createQuery(query);
			return q.getResultList();
		}
	}

	private <S> List<S> limit(EntityManager em, CriteriaQuery<S> query, Hints hints) {
		TypedQuery<S> q = em.createQuery(query);
		if (hints.getLimit() != null) {
			q.setMaxResults(hints.getLimit().getLimit());
		}
		return q.getResultList();
	}

	private <S> CriteriaQuery<S> filter(Class<S> model, CriteriaQuery<S> query, CriteriaBuilder cb, Root<S> root, Hints hints)
			throws SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Map<String, Object> filterDict = new HashMap<String, Object>();
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Filter filter : hints.getFilters()) {
			if (!ReflectUtils.hasProperty(model, filter.getName())) {
				continue;
			}
			if ("equals".equals(filter.getComparator())) {
				filterDict = exactFilter(model, filter, filterDict, hints);
				for (Entry<String, Object> entry : filterDict.entrySet()) {
					predicates.add(cb.equal(getColumnAttr(root, entry.getKey()), entry.getValue()));
				}
			} else {
				query = inexactFilter(model, query, cb, root, filter, hints);
			}
		}

		if (!predicates.isEmpty()) {
			query = query.where(predicates.toArray(new Predicate[predicates.size()]));
		}

		return query;
	}

	private <S> CriteriaQuery<S> inexactFilter(Class<S> model, CriteriaQuery<S> query, CriteriaBuilder cb, Root<S> root,
			Filter filter, Hints hints) {
		if (filter.isCaseSensitive()) {
			return query;
		}

		Predicate predicate = null;
		Path<String> path = getColumnAttr(root, filter.getName());
		if ("contains".equals(filter.getComparator())) {
			predicate = cb.like(path, String.format("%%%s%%", filter.getValue()));
		} else if ("startswith".equals(filter.getComparator())) {
			predicate = cb.like(path, String.format("%s%%", filter.getValue()));
		} else if ("endswith".equals(filter.getComparator())) {
			predicate = cb.like(path, String.format("%%%s", filter.getValue()));
		} else {
			return query;
		}

		Predicate oldPredicate = query.getRestriction();
		if (oldPredicate != null) {
			query.where(oldPredicate, predicate);
		} else {
			query.where(predicate);
		}
		return query;
	}

	private <S> Path<String> getColumnAttr(Root<S> root, String name) {
		if (name.equals("domain_id")) {
			return root.get("domain").<String> get("id");
		} else if (name.equals("project_id")) {
			return root.get("project").<String> get("id");
		} else if (name.equals("group_id")) {
			return root.get("group").<String> get("id");
		} else if (name.equals("role_id")) {
			return root.get("role").<String> get("id");
		} else if (name.equals("user_id")) {
			return root.get("user").<String> get("id");
		} else if (name.equals("region_id")) {
			return root.get("region").<String> get("id");
		} else if (name.equals("service_id")) {
			return root.get("service").<String> get("id");
		} else if (name.equals("endpoint_id")) {
			return root.get("endpoint").<String> get("id");
		} else if (name.equals("catalog_id")) {
			return root.get("catalog").<String> get("id");
		} else if (name.equals("trust_id")) {
			return root.get("trust").<String> get("id");
		} else if (name.equals("parent_id")) {
			return root.get("parent").<String> get("id");
		} else {
			return root.get(concentrateName(name));
		}
	}

	private String concentrateName(String orig) {
		if (orig.contains("_")) {
			String[] split = orig.split("_");
			orig = split[0];
			if (split.length >= 2) {
				for (String s : Arrays.copyOfRange(split, 1, split.length)) {
					orig += Character.toUpperCase(s.charAt(0)) + s.substring(1);
				}
			}
		}
		return orig;
	}

	private <S> Map<String, Object> exactFilter(Class<S> model, Filter filter, Map<String, Object> cumulativeFilterDict,
			Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String key = filter.getName();

		if (ReflectUtils.getReturnType(model, key).equals(Boolean.class)) {
			cumulativeFilterDict.put(key, Boolean.parseBoolean(String.valueOf(filter.getValue())));
		} else {
			cumulativeFilterDict.put(key, filter.getValue());
		}

		return cumulativeFilterDict;
	}

	protected Class<T> getEntityType() {
		return entityType;
	}
}
