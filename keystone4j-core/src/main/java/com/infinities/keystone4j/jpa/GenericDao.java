package com.infinities.keystone4j.jpa;

import java.util.List;

public interface GenericDao<T> {

	void persist(T transientInstance) throws Exception;

	void remove(T persistentInstance) throws Exception;

	T merge(T detachedInstance) throws Exception;

	T findById(Object id) throws Exception;

	List<T> findAll() throws Exception;
}
