package com.infinities.keystone4j.identity.action.group;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class ListGroupsAction extends AbstractGroupAction<List<Group>> {

	private final String domainid;
	private final String name;


	public ListGroupsAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String domainid,
			String name) {
		super(assignmentApi, tokenApi, identityApi);
		this.domainid = domainid;
		this.name = name;
	}

	@Override
	public List<Group> execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		Iterable<Group> groups = this.getIdentityApi().listGroups(domain.getId());

		List<Predicate<Group>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate<Group> filter = new Predicate<Group>() {

				@Override
				public boolean apply(Group g) {
					return g.getDomain() != null && domainid.equals(g.getDomain().getId());
				}
			};
			filters.add(filter);
		}

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
		return "list_groups";
	}
}
