package com.infinities.keystone4j.identity.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class ListUsersAction extends AbstractUserAction<List<User>> {

	private final String domainid;
	private final String email;
	private final Boolean enabled;
	private final String name;
	private HttpServletRequest request;


	public ListUsersAction(IdentityApi identityApi, String domainid, String email, Boolean enabled, String name) {
		super(identityApi);
		this.domainid = domainid;
		this.email = email;
		this.enabled = enabled;
		this.name = name;
	}

	@Override
	public List<User> execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		Iterable<User> users = this.getIdentityApi().listUsers(domain.getId());

		List<Predicate<User>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User u) {
					return u.getDomain() != null && domainid.equals(u.getDomain().getId());
				}
			};
			filters.add(filter);
		}

		if (!Strings.isNullOrEmpty(email)) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User u) {
					return email.equals(u.getEmail());
				}
			};
			filters.add(filter);
		}

		if (enabled != null) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User u) {
					return enabled.compareTo(u.getEnabled()) == 0;
				}
			};
			filters.add(filter);
		}

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User u) {
					return name.equals(u.getName());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<User> filter = Predicates.and(filters);

			users = Iterables.filter(users, filter);
		}

		return Lists.newArrayList(users);
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "list_users";
	}
}
