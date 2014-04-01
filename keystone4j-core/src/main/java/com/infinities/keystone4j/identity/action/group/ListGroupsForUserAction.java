package com.infinities.keystone4j.identity.action.group;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class ListGroupsForUserAction extends AbstractGroupAction<List<Group>> {

	private final String userid;
	private final String name;


	public ListGroupsForUserAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String userid,
			String name) {
		super(assignmentApi, tokenApi, identityApi);
		this.userid = userid;
		this.name = name;
	}

	@Override
	public List<Group> execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
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

	@Override
	public String getName() {
		return "list_groups_for_user";
	}
}
