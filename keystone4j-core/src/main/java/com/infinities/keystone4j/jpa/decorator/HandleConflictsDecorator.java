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
