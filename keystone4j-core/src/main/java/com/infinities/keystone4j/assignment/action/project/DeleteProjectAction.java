package com.infinities.keystone4j.assignment.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;

public class DeleteProjectAction extends AbstractProjectAction<Project> {

	private final String projectid;


	public DeleteProjectAction(AssignmentApi assignmentApi, String projectid) {
		super(assignmentApi);
		this.projectid = projectid;
	}

	@Override
	public Project execute(ContainerRequestContext request) {
		return this.getAssignmentApi().deleteProject(projectid);
	}

	@Override
	public String getName() {
		return "delete_project";
	}

}
