package com.infinities.keystone4j.assignment.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;

public class UpdateProjectAction extends AbstractProjectAction<Project> {

	private final String projectid;
	private final Project project;


	public UpdateProjectAction(AssignmentApi assignmentApi, String projectid, Project project) {
		super(assignmentApi);
		this.project = project;
		this.projectid = projectid;
	}

	@Override
	public Project execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(projectid, project);
		return this.getAssignmentApi().updateProject(projectid, project);
	}

	@Override
	public String getName() {
		return "update_project";
	}
}
