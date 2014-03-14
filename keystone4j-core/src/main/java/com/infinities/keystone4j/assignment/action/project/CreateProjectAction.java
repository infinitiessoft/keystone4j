package com.infinities.keystone4j.assignment.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;

public class CreateProjectAction extends AbstractProjectAction<Project> {

	private final Project project;


	public CreateProjectAction(AssignmentApi assignmentApi, Project project) {
		super(assignmentApi);
		this.project = project;
	}

	@Override
	public Project execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		project.setDomain(domain);
		Project ret = assignmentApi.createProject(project);
		return ret;
	}

	@Override
	public String getName() {
		return "create_project";
	}
}
