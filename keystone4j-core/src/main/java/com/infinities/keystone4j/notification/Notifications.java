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
package com.infinities.keystone4j.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jersey.repackaged.com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.utils.ReflectUtils;

public class Notifications {

	public static final String INVALIDATE_USER_TOKEN_PERSISTENCE = "invalidate_user_tokens";
	public static final String INVALIDATE_USER_PROJECT_TOKEN_PERSISTENCE = "invalidate_user_project_tokens";
	public static final String INVALIDATE_USER_OAUTH_CONSUMER_TOKENS = "invalidate_user_consumer_tokens";
	static Map<Actions, Map<String, List<NotificationCallback>>> _SUBSCRIBERS = Maps.newConcurrentMap();


	public static void registerEventCallback(Actions event, String resourceType, NotificationCallback callbackFns) {
		List<NotificationCallback> callbacks = new ArrayList<NotificationCallback>();
		callbacks.add(callbackFns);
		Map<String, List<NotificationCallback>> callbackMap = new HashMap<String, List<NotificationCallback>>();
		callbackMap.put(resourceType, callbacks);
		_SUBSCRIBERS.put(event, callbackMap);
	}

	public static <E> ManagerNotificationWrapper<E> created(NotifiableCommand<E> command, String resourceType) {
		return created(command, resourceType, true, 1, null);
	}

	public static <E> ManagerNotificationWrapper<E> created(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.created, resourceType, _public, resourceIdArgIndex,
				resultIdArgAttr);
	}

	public static <E> ManagerNotificationWrapper<E> updated(NotifiableCommand<E> command, String resourceType) {
		return updated(command, resourceType, true, 1, null);
	}

	public static <E> ManagerNotificationWrapper<E> updated(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.updated, resourceType, _public, resourceIdArgIndex,
				resultIdArgAttr);
	}

	public static <E> ManagerNotificationWrapper<E> disabled(NotifiableCommand<E> command, String resourceType) {
		return disabled(command, resourceType, true, 1, null);
	}

	public static <E> ManagerNotificationWrapper<E> disabled(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.disabled, resourceType, _public, resourceIdArgIndex,
				resultIdArgAttr);
	}

	public static <E> ManagerNotificationWrapper<E> deleted(NotifiableCommand<E> command, String resourceType) {
		return deleted(command, resourceType, true, 1, null);
	}

	public static <E> ManagerNotificationWrapper<E> deleted(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.deleted, resourceType, _public, resourceIdArgIndex,
				resultIdArgAttr);
	}

	public static <E> ManagerNotificationWrapper<E> internal(NotifiableCommand<E> command, String resourceType) {
		return internal(command, resourceType, false, 1, null);
	}

	public static <E> ManagerNotificationWrapper<E> internal(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.internal, resourceType, false, resourceIdArgIndex,
				resultIdArgAttr);
	}

	public static <E> ManagerNotificationWrapper<E> roleAssignment(NotifiableCommand<E> command, String resourceType,
			boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
		return new ManagerNotificationWrapper<E>(command, Actions.internal, resourceType, false, resourceIdArgIndex,
				resultIdArgAttr);
	}


	public static class Payload {

		private Object resourceInfo;


		public Object getResourceInfo() {
			return resourceInfo;
		}

		public void setResourceInfo(Object resourceInfo) {
			this.resourceInfo = resourceInfo;
		}
	}

	public static class ManagerNotificationWrapper<E> implements NonTruncatedCommand<E> {

		private final static Logger logger = LoggerFactory.getLogger(ManagerNotificationWrapper.class);
		private final NotifiableCommand<E> command;
		private final Actions operation;
		private final String resourceType;
		private boolean _public = true;
		private int resourceIdArgIndex = 1;
		private String resultIdArgAttr = null;


		// public=true, resourceIdArgIndex=1,resultIdArgAttr=null
		public ManagerNotificationWrapper(NotifiableCommand<E> command, Actions operation, String resourceType,
				boolean _public, int resourceIdArgIndex, String resultIdArgAttr) {
			this.command = command;
			this.operation = operation;
			this.resourceType = resourceType;
			this._public = _public;
			this.resourceIdArgIndex = resourceIdArgIndex;
			this.resultIdArgAttr = resultIdArgAttr;
		}

		@Override
		public E execute() throws Exception {
			E result = null;
			try {
				result = command.execute();
			} catch (Exception e) {
				throw e;
			}

			Object resourceId = null;
			if (!Strings.isNullOrEmpty(resultIdArgAttr)) {
				resourceId = ReflectUtils.reflact(result, resultIdArgAttr);
			} else {
				resourceId = command.getArgs(resourceIdArgIndex);
			}

			sendNotification(operation, resourceType, resourceId, _public);
			return result;
		}

		// _public =true
		private void sendNotification(Actions operation, String resourceType, Object resourceId, boolean _public)
				throws Exception {
			Payload payload = new Payload();
			payload.setResourceInfo(resourceId);
			String service = "identity";
			notifyEventCallbacks(service, resourceType, operation, payload);

			// TODO ignore notifier
			// if (_public) {
			// notifier = getNotifier();
			// if (notifier != null) {
			// String eventType = String.format("%s.%s.%s", service,
			// resourceType,
			// operation);
			// try {
			// notifier.info(context, eventType, payload);
			// } catch (Exception e) {
			// logger.error("Failed to send {} {} notification", resourceId,
			// eventType);
			// }
			// }
			// }

		}

		private void notifyEventCallbacks(String service, String resourceType, Actions operation, Payload payload)
				throws Exception {
			if (Notifications._SUBSCRIBERS.containsKey(operation)) {
				if (Notifications._SUBSCRIBERS.get(operation).containsKey(resourceType)) {
					for (NotificationCallback cb : Notifications._SUBSCRIBERS.get(operation).get(resourceType)) {
						logger.debug("Invoking callback {} for event {} {} {} for {}", new Object[] {
								cb.getClass().getName(), service, resourceType, operation, payload });
						cb.invoke(service, resourceType, operation, payload);
					}
				}
			}
		}
	}

	public static class CadfRoleAssignmentNotificationWrapper<E> implements NonTruncatedCommand<E> {

		public static String ROLE_ASSIGNMENT = "role_assignment";
		private final RoleAssignmentCommand<E> command;
		// private final String operation;
		private final String roleId;


		public CadfRoleAssignmentNotificationWrapper(RoleAssignmentCommand<E> command, Actions operation, String roleId) {
			this.command = command;
			// this.operation = String.format("%s.%s", operation,
			// ROLE_ASSIGNMENT);
			this.roleId = roleId;
		}

		@Override
		public E execute() throws Exception {
			boolean inherited = command.getInheritedToProjects();
			// KeystoneContext context = command.getContext();
			// initiator = getRequestAuditInfo(context);

			AuditKwargs auditKwargs = new AuditKwargs();
			if (!Strings.isNullOrEmpty(command.getProjectId())) {
				auditKwargs.setProjectId(command.getProjectId());
			} else if (!Strings.isNullOrEmpty(command.getDomainId())) {
				auditKwargs.setDomainId(command.getDomainId());
			}

			if (!Strings.isNullOrEmpty(command.getUserId())) {
				auditKwargs.setUserId(command.getUserId());
			} else if (!Strings.isNullOrEmpty(command.getGroupId())) {
				auditKwargs.setGroupId(command.getGroupId());
			}

			auditKwargs.setInheritedToProjects(inherited);
			auditKwargs.setRole(roleId);

			E result = null;
			try {
				result = command.execute();
			} catch (Exception e) {
				// ignore _send_audit_notification(self.action, initiator,
				// taxonomy.OUTCOME_FAILURE)
				// sendAuditNotification(operation,initiator)
				throw e;
			}

			// _send_audit_notification(self.action, initiator,
			// taxonomy.OUTCOME_SUCCESS)

			return result;
		}


		public static class AuditKwargs {

			private String projectId;
			private String domainId;
			private String userId;
			private String groupId;
			private boolean inheritedToProjects;
			private String role;


			public String getProjectId() {
				return projectId;
			}

			public void setProjectId(String projectId) {
				this.projectId = projectId;
			}

			public String getDomainId() {
				return domainId;
			}

			public void setDomainId(String domainId) {
				this.domainId = domainId;
			}

			public String getUserId() {
				return userId;
			}

			public void setUserId(String userId) {
				this.userId = userId;
			}

			public String getGroupId() {
				return groupId;
			}

			public void setGroupId(String groupId) {
				this.groupId = groupId;
			}

			public boolean isInheritedToProjects() {
				return inheritedToProjects;
			}

			public void setInheritedToProjects(boolean inheritedToProjects) {
				this.inheritedToProjects = inheritedToProjects;
			}

			public String getRole() {
				return role;
			}

			public void setRole(String role) {
				this.role = role;
			}

		}
	}
}
