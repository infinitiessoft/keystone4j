package com.infinities.keystone4j.assignment.controller.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	private final String projectid;
	private final Project project;


	public UpdateProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String projectid,
			Project project) {
		super(assignmentApi, tokenProviderApi);
		this.project = project;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext context) {
		requireMatchingId(projectid, project);
		requireMatchingDomainId(project, assignmentApi.getProject(projectid));
		Project ref = this.getAssignmentApi().updateProject(projectid, project);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_project";
	}
}
