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
package com.infinities.keystone4j.contrib.revoke.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jersey.repackaged.com.google.common.collect.Lists;

import com.google.common.base.Strings;
import com.infinities.keystone4j.contrib.revoke.model.Model.TokenValues;
import com.infinities.keystone4j.utils.ReflectUtils;

public class RevokeTree {

	private final Map<String, Object> revokeMap;

	// private final static List<String> _NAMES;
	private final static List<String> _EVENT_NAMES;
	public final static List<String> REVOKE_KEYS;

	static {
		List<String> names = new ArrayList<String>();
		names.add("trust_id");
		names.add("consumer_id");
		names.add("access_token_id");
		names.add("audit_id");
		names.add("audit_chain_id");
		names.add("expires_id");
		names.add("domain_id");
		names.add("project_id");
		names.add("user_id");
		names.add("role_id");
		// _NAMES = Collections.unmodifiableList(names);

		List<String> eventNames = new ArrayList<String>();
		eventNames.addAll(names);
		eventNames.add("domain_scope_id");
		_EVENT_NAMES = Collections.unmodifiableList(eventNames);

		List<String> eventArgs = new ArrayList<String>();
		eventArgs.add("issued_before");
		eventArgs.add("revoked_at");

		List<String> revokeKeys = new ArrayList<String>();
		revokeKeys.addAll(names);
		revokeKeys.addAll(eventArgs);
		REVOKE_KEYS = Collections.unmodifiableList(revokeKeys);
	}


	// events=null
	public RevokeTree(List<RevokeEvent> revokeEvents) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		revokeMap = new HashMap<String, Object>();
		addEvents(revokeEvents);
	}

	public List<RevokeEvent> addEvents(List<RevokeEvent> revokeEvents) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<RevokeEvent> events = new ArrayList<RevokeEvent>();
		for (RevokeEvent event : revokeEvents) {
			events.add(addEvent(event));
		}
		return events;
	}

	public RevokeEvent addEvent(RevokeEvent event) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (String key : getAttrKeys(event)) {
			revokeMap.put(key, new HashMap<String, Object>());
		}

		revokeMap.put("issued_before",
				event.getIssuedBefore().after(revokeMap.get("issued_before")) ? event.getIssuedBefore()
						: (Calendar) revokeMap.get("issued_before"));
		return event;
	}

	public void removeEvent(RevokeEvent event) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		List<Stack> stacks = new ArrayList<Stack>();
		Map<String, Object> revokeMap = this.revokeMap;
		boolean isBreak = false;

		for (String name : _EVENT_NAMES) {
			String key = keyForName(event, name);
			@SuppressWarnings("unchecked")
			Map<String, Object> nxt = revokeMap.containsKey(key) ? (Map<String, Object>) revokeMap.get(key) : null;
			if (nxt == null) {
				isBreak = true;
				break;
			}
			Stack stack = new Stack();
			stack.setKey(key);
			stack.setRevokeMap(revokeMap);
			stack.setNxt(nxt);
			stacks.add(stack);
			revokeMap = nxt;
		}

		if (!isBreak) {
			if (event.getIssuedBefore().compareTo((Calendar) revokeMap.get("issued_before")) == 0) {
				revokeMap.remove("issued_before");
			}
		}

		for (Stack stack : Lists.reverse(stacks)) {
			if (stack.getNxt() == null) {
				stack.getRevokeMap().remove(stack.getKey());
			}
		}

	}

	public List<String> getAttrKeys(RevokeEvent event) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<String> attrKeys = new ArrayList<String>();

		for (String key : _EVENT_NAMES) {
			attrKeys.add(keyForName(event, key));
		}
		return attrKeys;
	}

	public String keyForName(RevokeEvent event, String key) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object attr = ReflectUtils.reflact(this, key);
		String value = attr == null ? "*" : String.valueOf(attr);
		return String.format("%s=%s", key, value);
	}

	public String nullToStar(String original) {
		if (Strings.isNullOrEmpty(original)) {
			return "*";
		}
		return original;
	}

	public boolean isRevoked(TokenValues tokenData) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		Map<String, List<String>> alternatives = new HashMap<String, List<String>>();
		List<String> userIds = new ArrayList<String>();
		userIds.add("user_id");
		userIds.add("trustor_id");
		userIds.add("trustee_id");
		alternatives.put("user_id", userIds);

		List<String> domainIds = new ArrayList<String>();
		domainIds.add("identity_domain_id");
		domainIds.add("assignment_domain_id");
		alternatives.put("domain_id", domainIds);

		List<String> domainScopeIds = new ArrayList<String>();
		domainScopeIds.add("assignment_domain_id");
		alternatives.put("domain_scope_id", domainIds);

		List<Object> partialMatches = new ArrayList<Object>();
		partialMatches.add(this.revokeMap);

		for (String name : _EVENT_NAMES) {
			List<Object> bundle = new ArrayList<Object>();
			String wildcard = String.format("%s=*", name);

			for (int index = 0; index < partialMatches.size(); index++) {
				@SuppressWarnings("unchecked")
				Map<String, Object> tree = (Map<String, Object>) partialMatches.get(index);
				Object leaf = tree.get(wildcard);
				if (leaf != null) {
					bundle.add(leaf);
				}
				if ("role_id".equals(name)) {
					for (String roleId : tokenData.getRoles()) {
						Object leaf2 = tree.get(String.format("role_id=%s", roleId));
						if (leaf2 != null) {
							bundle.add(leaf2);
						}
					}
				} else {
					List<String> altNames = alternatives.get(name);
					if (altNames == null) {
						altNames = new ArrayList<String>();
						altNames.add(name);
					}

					for (String altName : altNames) {
						Object leaf3 = tree.get(String.format("%s=%s", name, ReflectUtils.reflact(tokenData, altName)));
						if (leaf3 != null) {
							bundle.add(leaf3);
						}
					}
				}
			}
			partialMatches = new ArrayList<Object>();
			partialMatches.addAll(bundle);

			if (partialMatches.isEmpty()) {
				return false;
			}
		}

		for (Object leaf : partialMatches) {
			try {
				Calendar leafCalendar = (Calendar) ReflectUtils.reflact(leaf, "issued_before");
				Calendar tokenCalendar = (Calendar) ReflectUtils.reflact(leaf, "issued_at");
				if (leafCalendar.after(tokenCalendar)) {
					return true;
				}
			} catch (Exception e) {

			}
		}

		return false;
	}


	public class Stack {

		private Map<String, Object> revokeMap;
		private String key;
		private Map<String, Object> nxt;


		public Map<String, Object> getRevokeMap() {
			return revokeMap;
		}

		public void setRevokeMap(Map<String, Object> revokeMap) {
			this.revokeMap = revokeMap;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Map<String, Object> getNxt() {
			return nxt;
		}

		public void setNxt(Map<String, Object> nxt) {
			this.nxt = nxt;
		}

	}

}
