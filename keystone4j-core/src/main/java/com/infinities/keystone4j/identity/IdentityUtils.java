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
package com.infinities.keystone4j.identity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class IdentityUtils {

	private final Map<String, IdentityDriver> driverMap = new ConcurrentHashMap<String, IdentityDriver>();
	private IdentityDriver defaultDriver;


	// TODO don't allow multiple identity driver now.
	public IdentityDriver selectIdentityDirver(String domainid) {
		if (driverMap.containsKey(domainid)) {
			return driverMap.get(domainid);
		} else {
			return defaultDriver;
		}
	}

	// public User clearDomainid(User user) {
	// String domainid = user.getDomain().getId();
	// // user.setDomain(null);
	// if (!Strings.isNullOrEmpty(domainid) && !driverMap.containsKey(domainid))
	// {
	// throw Exceptions.DomainNotFoundException.getInstance(null, domainid);
	// }
	//
	// return user;
	// }

	// public Group clearDomainid(Group group) {
	// String domainid = group.getDomain().getId();
	// // group.setDomain(null);
	// if (!Strings.isNullOrEmpty(domainid) && !driverMap.containsKey(domainid))
	// {
	// throw Exceptions.DomainNotFoundException.getInstance(null, domainid);
	// }
	//
	// return group;
	// }
}
