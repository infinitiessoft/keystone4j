package com.infinities.keystone4j.assignment.action.project;

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
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class GetProjectUsersAction extends AbstractProjectAction<List<User>> {

	private final String projectid;
	private final String name;
	private final Boolean enabled;
	private final TokenApi tokenApi;


	public GetProjectUsersAction(AssignmentApi assignmentApi, TokenApi tokenApi, String projectid, String name,
			Boolean enabled) {
		super(assignmentApi);
		this.projectid = projectid;
		this.name = name;
		this.enabled = enabled;
		this.tokenApi = tokenApi;
	}

	@Override
	public List<User> execute(ContainerRequestContext request) {

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		new KeystoneUtils().assertAdmin(tokenApi, context);
		Iterable<User> users = this.getAssignmentApi().listUsersForProject(projectid);

		List<Predicate<User>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User p) {
					return name.equals(p.getName());
				}
			};
			filters.add(filter);
		}

		if (enabled != null) {
			Predicate<User> filter = new Predicate<User>() {

				@Override
				public boolean apply(User p) {
					return enabled.compareTo(p.getEnabled()) == 0;
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

	@Override
	public String getName() {
		return "list_user_projects";
	}
}
