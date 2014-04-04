package com.infinities.keystone4j.assignment.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.assignment.Project;

public class GetProjectAction extends AbstractProjectAction<Project> {

	private final String projectid;


	public GetProjectAction(AssignmentApi assignmentApi, String projectid) {
		super(assignmentApi);
		this.projectid = projectid;
	}

	@Override
	public Project execute(ContainerRequestContext request) {
		return this.getAssignmentApi().getProject(projectid);
	}

	@Override
	public String getName() {
		return "get_project";
	}
}
