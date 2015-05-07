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
package com.infinities.keystone4j.contrib.revoke.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.contrib.revoke.driver.RevokeDriver;
import com.infinities.keystone4j.contrib.revoke.model.Model.TokenValues;
import com.infinities.keystone4j.contrib.revoke.model.RevokeEvent;
import com.infinities.keystone4j.contrib.revoke.model.RevokeTree;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.notification.Actions;
import com.infinities.keystone4j.notification.NotificationCallback;
import com.infinities.keystone4j.notification.Notifications;
import com.infinities.keystone4j.notification.Notifications.Payload;

public class RevokeApiImpl implements RevokeApi {

	private final RevokeDriver driver;


	public RevokeApiImpl(RevokeDriver driver) {
		this.driver = driver;
		registerListeners();
	}

	private void registerListeners() {
		Map<Actions, List<Entry<String, ? extends NotificationCallback>>> callbacks = new HashMap<Actions, List<Entry<String, ? extends NotificationCallback>>>();
		List<Entry<String, ? extends NotificationCallback>> deletedList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		deletedList.add(Maps.immutableEntry("OS-TRUST:trust", new TrustCallback()));
		deletedList.add(Maps.immutableEntry("OS-OAUTH1:consumer", new ConsumerCallback()));
		deletedList.add(Maps.immutableEntry("OS-OAUTH1:access_token", new AccessTokenCallback()));
		deletedList.add(Maps.immutableEntry("role", new RoleCallback()));
		deletedList.add(Maps.immutableEntry("user", new UserCallback()));
		deletedList.add(Maps.immutableEntry("project", new ProjectCallback()));
		callbacks.put(Actions.deleted, deletedList);
		List<Entry<String, ? extends NotificationCallback>> disabledList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		disabledList.add(Maps.immutableEntry("domain", new DomainCallback()));
		disabledList.add(Maps.immutableEntry("user", new UserCallback()));
		disabledList.add(Maps.immutableEntry("project", new ProjectCallback()));
		callbacks.put(Actions.disabled, disabledList);
		List<Entry<String, ? extends NotificationCallback>> internalList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		disabledList.add(Maps.immutableEntry(Notifications.INVALIDATE_USER_TOKEN_PERSISTENCE, new UserCallback()));
		callbacks.put(Actions.internal, internalList);

		for (Entry<Actions, List<Entry<String, ? extends NotificationCallback>>> entry : callbacks.entrySet()) {
			Actions event = entry.getKey();
			List<Entry<String, ? extends NotificationCallback>> cbInfos = entry.getValue();
			for (Entry<String, ? extends NotificationCallback> cbInfo : cbInfos) {
				String resourceType = cbInfo.getKey();
				NotificationCallback callbackFns = cbInfo.getValue();
				Notifications.registerEventCallback(event, resourceType, callbackFns);
			}
		}
	}

	@Override
	public void revokeByUser(String userid) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setUserId(userid);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByExpiration(String userId, Calendar expiresAt, String projectId, String domainId) {
		assertNotDomainAndProjectScoped(domainId, projectId);
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setUserId(userId);
		revokeEvent.setExpiresAt(expiresAt);
		revokeEvent.setDomainId(domainId);
		revokeEvent.setProjectId(projectId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByAuditId(String auditId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setAuditId(auditId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByAuditChainId(String auditChainId, String projectId, String domainId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setAuditChainId(auditChainId);
		revokeEvent.setDomainId(domainId);
		revokeEvent.setProjectId(projectId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByGrant(String roleId, String userId, String domainId, String projectId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setDomainId(domainId);
		revokeEvent.setProjectId(projectId);
		revokeEvent.setUserId(userId);
		revokeEvent.setRoleId(roleId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	private void assertNotDomainAndProjectScoped(String domainid, String projectid) {
		if (!Strings.isNullOrEmpty(domainid) && !Strings.isNullOrEmpty(projectid)) {
			String msg = "The revoke call must not have both domain_id and project_id. This is a bug in the Keystone server. The current request is aborted.";
			throw Exceptions.UnexpectedException.getInstance(msg);
		}
	}

	@Override
	public void revokeByUserAndProject(String userId, String projectId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setProjectId(projectId);
		revokeEvent.setUserId(userId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByProjectRoleAssignment(String projectId, String roleId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setProjectId(projectId);
		revokeEvent.setRoleId(roleId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void revokeByDomainRoleAssignment(String domainId, String roleId) {
		RevokeEvent revokeEvent = new RevokeEvent();
		revokeEvent.setDomainId(domainId);
		revokeEvent.setRoleId(roleId);
		revokeEvent.init();
		revoke(revokeEvent);
	}

	@Override
	public void checkToken(TokenValues tokenValues) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (getRevokeTree().isRevoked(tokenValues)) {
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}
	}

	private RevokeTree getRevokeTree() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		List<RevokeEvent> events = driver.getEvents(null);
		RevokeTree revokeTree = new RevokeTree(events);
		return revokeTree;
	}

	@Override
	public void revoke(RevokeEvent event) {
		driver.revoke(event);

	}


	public class UserCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			revokeByUser((String) payload.getResourceInfo());
		}

		// @Override
		// public String getName() {
		// return "_user_callback";
		// }

	}

	public class RoleCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setRoleId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_role_callback";
		// }

	}

	public class ProjectCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setProjectId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_project_callback";
		// }

	}

	public class DomainCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setDomainId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_domain_callback";
		// }

	}

	public class TrustCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setTrustId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_trust_callback";
		// }

	}

	public class ConsumerCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setConsumerId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_consumer_callback";
		// }

	}

	public class AccessTokenCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) {
			RevokeEvent revokeEvent = new RevokeEvent();
			revokeEvent.setAccessTokenId((String) payload.getResourceInfo());
			revokeEvent.init();
			revoke(revokeEvent);
		}

		// @Override
		// public String getName() {
		// return "_access_token_callback";
		// }
	}

}
