package com.infinities.keystone4j.identity.action.group;

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
import com.infinities.keystone4j.identity.model.Group;

public class ListGroupsForUserAction extends AbstractGroupAction<List<Group>> {

	private String userid;
	private String name;
	private HttpServletRequest request;


	public ListGroupsForUserAction(IdentityApi identityApi, String userid, String name) {
		super(identityApi);
		this.userid = userid;
		this.name = name;
	}

	@Override
	public List<Group> execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		Iterable<Group> groups = this.getIdentityApi().listGroupsForUser(userid, domain.getId());

		List<Predicate<Group>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<Group> filter = new Predicate<Group>() {

				@Override
				public boolean apply(Group g) {
					return name.equals(g.getName());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Group> filter = Predicates.and(filters);
			groups = Iterables.filter(groups, filter);
		}

		return Lists.newArrayList(groups);
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
