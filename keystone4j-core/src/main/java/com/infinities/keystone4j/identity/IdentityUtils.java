package com.infinities.keystone4j.identity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

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

	public User clearDomainid(User user) {
		String domainid = user.getDomain().getId();
		user.setDomain(null);
		if (!Strings.isNullOrEmpty(domainid) && !driverMap.containsKey(domainid)) {
			throw Exceptions.DomainNotFoundException.getInstance(null, domainid);
		}

		return user;
	}

	public Group clearDomainid(Group group) {
		String domainid = group.getDomain().getId();
		group.setDomain(null);
		if (!Strings.isNullOrEmpty(domainid) && !driverMap.containsKey(domainid)) {
			throw Exceptions.DomainNotFoundException.getInstance(null, domainid);
		}

		return group;
	}
}
