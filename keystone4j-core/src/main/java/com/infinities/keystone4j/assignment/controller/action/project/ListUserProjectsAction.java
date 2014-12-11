package com.infinities.keystone4j.assignment.controller.action.project;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListUserProjectsAction extends AbstractProjectAction implements FilterProtectedAction<Project> {

	private final String userid;


	public ListUserProjectsAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String userid) {
		super(assignmentApi, tokenProviderApi);
		this.userid = userid;
	}

	@Override
	public CollectionWrapper<Project> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Project> projects = this.getAssignmentApi().listProjectsForUser(userid, hints);
		CollectionWrapper<Project> wrapper = wrapCollection(request, projects, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_user_projects";
	}
}
