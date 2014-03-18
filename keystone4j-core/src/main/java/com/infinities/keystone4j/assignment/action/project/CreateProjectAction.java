package com.infinities.keystone4j.assignment.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.token.TokenApi;

public class CreateProjectAction extends AbstractProjectAction<Project> {

	private final Project project;
	private final TokenApi tokenApi;


	public CreateProjectAction(AssignmentApi assignmentApi, TokenApi tokenApi, Project project) {
		super(assignmentApi);
		this.tokenApi = tokenApi;
		this.project = project;
	}

	@Override
	public Project execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		normalizeDomainid(context, project);
		Project ret = assignmentApi.createProject(project);
		return ret;
	}

	private void normalizeDomainid(KeystoneContext context, Project project) {
		if (Strings.isNullOrEmpty(project.getDomainid())) {
			Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
			project.setDomain(domain);
		}
	}

	@Override
	public String getName() {
		return "create_project";
	}
}
