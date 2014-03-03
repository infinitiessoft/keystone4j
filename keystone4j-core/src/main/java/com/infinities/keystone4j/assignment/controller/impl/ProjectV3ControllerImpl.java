package com.infinities.keystone4j.assignment.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.action.project.CreateProjectAction;
import com.infinities.keystone4j.assignment.action.project.DeleteProjectAction;
import com.infinities.keystone4j.assignment.action.project.GetProjectAction;
import com.infinities.keystone4j.assignment.action.project.ListProjectsAction;
import com.infinities.keystone4j.assignment.action.project.ListUserProjectsAction;
import com.infinities.keystone4j.assignment.action.project.UpdateProjectAction;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.ProjectWrapper;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class ProjectV3ControllerImpl implements ProjectV3Controller {

	private final AssignmentApi assignmentApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	public ProjectV3ControllerImpl(AssignmentApi assignmentApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public ProjectWrapper createProject(Project project) {
		Action<Project> command = new PolicyCheckDecorator<Project>(new CreateProjectAction(assignmentApi, project), null,
				tokenApi, policyApi);
		Project ret = command.execute();
		return new ProjectWrapper(ret);
	}

	@Override
	public ProjectsWrapper listProjects(String domainid, String name, Boolean enabled, int page, int perPage) {
		Action<List<Project>> command = new FilterCheckDecorator<List<Project>>(new PaginateDecorator<Project>(
				new ListProjectsAction(assignmentApi, domainid, name, enabled), page, perPage));

		List<Project> ret = command.execute();
		return new ProjectsWrapper(ret);
	}

	@Override
	public ProjectsWrapper listUserProjects(String userid, Boolean enabled, String name, int page, int perPage) {
		Action<List<Project>> command = new FilterCheckDecorator<List<Project>>(new PaginateDecorator<Project>(
				new ListUserProjectsAction(assignmentApi, userid, name, enabled), page, perPage));

		List<Project> ret = command.execute();
		return new ProjectsWrapper(ret);
	}

	@Override
	public ProjectWrapper getProject(String projectid) {
		Action<Project> command = new PolicyCheckDecorator<Project>(new GetProjectAction(assignmentApi, projectid), null,
				tokenApi, policyApi);
		Project ret = command.execute();
		return new ProjectWrapper(ret);
	}

	@Override
	public ProjectWrapper updateProject(String projectid, Project project) {
		Action<Project> command = new PolicyCheckDecorator<Project>(new UpdateProjectAction(assignmentApi, projectid,
				project), null, tokenApi, policyApi);
		Project ret = command.execute();
		return new ProjectWrapper(ret);
	}

	@Override
	public void deleteProject(String projectid) {
		Action<Project> command = new PolicyCheckDecorator<Project>(new DeleteProjectAction(assignmentApi, projectid), null,
				tokenApi, policyApi);
		command.execute();
	}
}
