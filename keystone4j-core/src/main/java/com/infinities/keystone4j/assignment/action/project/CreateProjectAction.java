package com.infinities.keystone4j.assignment.action.project;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;

public class CreateProjectAction extends AbstractProjectAction<Project> {

	private final Project project;
	private HttpServletRequest request;


	public CreateProjectAction(AssignmentApi assignmentApi, Project project) {
		super(assignmentApi);
		this.project = project;
	}

	@Override
	public Project execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		project.setDomain(domain);
		Project ret = assignmentApi.createProject(project);
		return ret;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "create_project";
	}
}
