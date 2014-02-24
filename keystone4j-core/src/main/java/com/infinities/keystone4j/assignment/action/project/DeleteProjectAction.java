package com.infinities.keystone4j.assignment.action.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;

public class DeleteProjectAction extends AbstractProjectAction<Project> {

	private String projectid;


	public DeleteProjectAction(AssignmentApi assignmentApi, String projectid) {
		super(assignmentApi);
		this.projectid = projectid;
	}

	@Override
	public Project execute() {
		return this.getAssignmentApi().deleteProject(projectid);
	}

}
