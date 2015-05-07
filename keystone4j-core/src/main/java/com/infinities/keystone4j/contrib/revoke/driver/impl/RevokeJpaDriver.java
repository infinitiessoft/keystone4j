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
package com.infinities.keystone4j.contrib.revoke.driver.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.driver.RevokeDriver;
import com.infinities.keystone4j.contrib.revoke.model.RevokeEvent;
import com.infinities.keystone4j.jpa.impl.RevocationEventDao;
import com.infinities.keystone4j.model.contrib.revoke.RevocationEvent;

public class RevokeJpaDriver implements RevokeDriver {

	private final RevocationEventDao dao;


	public RevokeJpaDriver() {
		dao = new RevocationEventDao();
	}

	@Override
	public List<RevokeEvent> getEvents(Calendar lastFetch) {
		pruneExpiredEvents();
		List<RevocationEvent> revocationEvents = dao.findAll(lastFetch);
		List<RevokeEvent> events = new ArrayList<RevokeEvent>();
		for (RevocationEvent event : revocationEvents) {
			RevokeEvent record = new RevokeEvent();
			// record.setId(UUID.randomUUID().toString());
			record.setTrustId(event.getTrustId());
			record.setConsumerId(event.getConsumerId());
			record.setAccessTokenId(event.getAccessTokenId());
			record.setAuditId(event.getAuditId());
			record.setAuditChainId(event.getAuditChainId());
			record.setExpiresAt(event.getExpiresAt());
			record.setDomainId(event.getDomainId());
			record.setProjectId(event.getProjectId());
			record.setUserId(event.getUserId());
			record.setRoleId(event.getRoleId());
			record.setIssuedBefore(event.getIssuedBefore());
			record.setRevokedAt(event.getRevokedAt());
			events.add(record);
		}

		return events;
	}

	private void pruneExpiredEvents() {
		Calendar oldest = getRevokedBeforeCutoffTime();
		dao.removeByCutoffTime(oldest);
	}

	private Calendar getRevokedBeforeCutoffTime() {
		int expiration = Config.Instance.getOpt(Config.Type.token, "expiration").asInteger();
		int buffer = Config.Instance.getOpt(Config.Type.revoke, "expiration_buffer").asInteger();
		int expireDelta = expiration + buffer;
		Calendar oldest = Calendar.getInstance();
		oldest.add(Calendar.SECOND, expireDelta);
		return oldest;
	}

	@Override
	public void revoke(RevokeEvent event) {
		RevocationEvent record = new RevocationEvent();
		record.setId(UUID.randomUUID().toString());
		record.setTrustId(event.getTrustId());
		record.setConsumerId(event.getConsumerId());
		record.setAccessTokenId(event.getAccessTokenId());
		record.setAuditId(event.getAuditId());
		record.setAuditChainId(event.getAuditChainId());
		record.setExpiresAt(event.getExpiresAt());
		record.setDomainId(event.getDomainId());
		record.setProjectId(event.getProjectId());
		record.setUserId(event.getUserId());
		record.setRoleId(event.getRoleId());
		record.setIssuedBefore(event.getIssuedBefore());
		record.setRevokedAt(event.getRevokedAt());
		dao.persist(record);
	}

}
