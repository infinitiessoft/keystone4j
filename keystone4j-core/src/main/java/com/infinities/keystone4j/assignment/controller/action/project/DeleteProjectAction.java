package com.infinities.keystone4j.assignment.controller.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	private final String projectid;


	public DeleteProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String projectid) {
		super(assignmentApi, tokenProviderApi);
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext context) {
		this.getAssignmentApi().deleteProject(projectid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_project";
	}

}
