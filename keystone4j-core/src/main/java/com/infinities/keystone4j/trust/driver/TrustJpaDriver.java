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
package com.infinities.keystone4j.trust.driver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.NoResultException;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.TrustDao;
import com.infinities.keystone4j.jpa.impl.TrustRoleDao;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.trust.TrustDriver;

public class TrustJpaDriver implements TrustDriver {

	private static final int MAXIMUM_CONSUME_ATTEMPTS = 10;
	private final TrustDao trustDao;
	private final TrustRoleDao trustRoleDao;


	public TrustJpaDriver() {
		trustDao = new TrustDao();
		trustRoleDao = new TrustRoleDao();
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public Trust createTrust(String trustid, Trust trust, List<Role> roles) {
		trust.setId(trustid);
		trustDao.persist(trust);
		List<Role> addedRoles = new ArrayList<Role>();
		for (Role role : roles) {
			TrustRole trustRole = new TrustRole();
			trustRole.setTrustId(trustid);
			trustRole.setRoleId(role.getId());
			addedRoles.add(role);
			trustRoleDao.persist(trustRole);
		}
		trust.setRoles(addedRoles);
		return trust;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public void consumeUse(String trustid) throws InterruptedException {
		for (int attempt = 0; attempt < MAXIMUM_CONSUME_ATTEMPTS; attempt++) {
			Integer remainingUses = null;
			try {
				Integer queryResult = trustDao.findByIdWithNonDeleted(trustid).getRemainingUses();
				remainingUses = queryResult;
			} catch (NoResultException e) {
				throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
			}

			if (remainingUses == null) {
				return;
			} else if (remainingUses > 0) {
				List<Trust> rowsAffected = trustDao.findByIdWithRemainingUses(trustid, remainingUses);
				for (Trust trust : rowsAffected) {
					trust.setRemainingUses(trust.getRemainingUses() - 1);
					trustDao.merge(trust);
				}
				if (rowsAffected.size() == 1) {
					return;
				}
			} else {
				throw Exceptions.TrustUseLimitReached.getInstance(null, trustid);
			}
			Thread.sleep(0);
		}

		throw Exceptions.TrustConsumeMaximumAttempt.getInstance(null, trustid);
	}

	@Override
	public Trust getTrust(String trustid, boolean deleted) {
		Trust ref = null;
		try {
			ref = trustDao.findById(trustid, deleted);
		} catch (NoResultException e) {
			return null;
		}
		if (ref == null) {
			return null;
		}

		if (ref.getExpiresAt() != null && !deleted) {
			Calendar now = Calendar.getInstance();
			Calendar expiresAt = ref.getExpiresAt();
			if (now.after(expiresAt)) {
				return null;
			}
		}

		if (ref.getRemainingUses() != null && !deleted) {
			if (ref.getRemainingUses() <= 0) {
				return null;
			}
		}
		addRoles(trustid, ref);
		return ref;
	}

	private void addRoles(String trustid, Trust ref) {
		List<Role> roles = new ArrayList<Role>();
		for (TrustRole trustRole : trustRoleDao.findByTrustId(trustid)) {
			Role role = new Role();
			role.setId(trustRole.getRoleId());
			roles.add(role);
		}
		ref.setRoles(roles);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public List<Trust> listTrusts() {
		List<Trust> trusts = trustDao.listUndeleted();
		return trusts;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public List<Trust> listTrustsForTrustee(String trusteeid) {
		return trustDao.listTrustsForTrustee(trusteeid);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public List<Trust> listTrustsForTrustor(String trustorid) {
		return trustDao.listTrustsForTrustor(trustorid);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='trust')
	@Override
	public void deleteTrust(String trustid) {
		trustDao.remove(trustid);
	}

}
