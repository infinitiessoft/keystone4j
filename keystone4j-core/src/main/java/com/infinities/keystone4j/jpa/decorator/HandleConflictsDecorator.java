package com.infinities.keystone4j.jpa.decorator;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.AbstractDao;

public class HandleConflictsDecorator<T> {

	private final static Logger logger = LoggerFactory.getLogger(HandleConflictsDecorator.class);
	private final String conflictType;
	private final AbstractDao<T> dao;


	public HandleConflictsDecorator(AbstractDao<T> dao, String conflictType) {
		this.dao = dao;
		this.conflictType = conflictType;
	}

	public void persist(T t) {
		try {
			dao.persist(t);
		} catch (EntityExistsException e) {
			logger.debug("Conflict {}", new Object[] { conflictType }, e);
			throw Exceptions.ConflictException.getInstance(null, conflictType, "Duplicate Entry");
		} catch (PersistenceException e) {
			logger.debug("Conflict {}", new Object[] { conflictType }, e);
			throw Exceptions.UnexpectedException.getInstance(String.format(
					"An unexpected error occured when trying to store %s", conflictType));
		}
	}

}
