package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.controller.action.project.CreateProjectAction;
import com.infinities.keystone4j.assignment.controller.action.project.DeleteProjectAction;
import com.infinities.keystone4j.assignment.controller.action.project.GetProjectAction;
import com.infinities.keystone4j.assignment.controller.action.project.ListProjectsAction;
import com.infinities.keystone4j.assignment.controller.action.project.ListUserProjectsAction;
import com.infinities.keystone4j.assignment.controller.action.project.UpdateProjectAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.ProjectV3 20141209

public class ProjectV3ControllerImpl extends BaseController implements ProjectV3Controller {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public ProjectV3ControllerImpl(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<Project> createProject(Project project) throws Exception {
		ProtectedAction<Project> command = new ProtectedDecorator<Project>(new CreateProjectAction(assignmentApi,
				tokenProviderApi, policyApi, project), tokenProviderApi, policyApi);
		MemberWrapper<Project> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Project> listProjects() throws Exception {
		FilterProtectedAction<Project> command = new FilterProtectedDecorator<Project>(new ListProjectsAction(assignmentApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Project> ret = command.execute(getRequest(), "domain_id", "enabled", "name", "parent_id");
		return ret;
	}

	@Override
	public CollectionWrapper<Project> listUserProjects(String userid) throws Exception {
		FilterProtectedAction<Project> command = new FilterProtectedDecorator<Project>(new ListUserProjectsAction(
				assignmentApi, tokenProviderApi, policyApi, userid), tokenProviderApi, policyApi);
		CollectionWrapper<Project> ret = command.execute(getRequest(), "enabled", "name");
		return ret;
	}

	@Override
	public MemberWrapper<Project> getProject(String projectid) throws Exception {
		Project ref = getMemberFromDriver(projectid);
		ProtectedAction<Project> command = new ProtectedDecorator<Project>(new GetProjectAction(assignmentApi,
				tokenProviderApi, policyApi, projectid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Project> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Project> updateProject(String projectid, Project project) throws Exception {
		Project ref = getMemberFromDriver(projectid);
		ProtectedAction<Project> command = new ProtectedDecorator<Project>(new UpdateProjectAction(assignmentApi,
				tokenProviderApi, policyApi, projectid, project), tokenProviderApi, policyApi, ref);
		MemberWrapper<Project> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteProject(String projectid) throws Exception {
		Project ref = getMemberFromDriver(projectid);
		ProtectedAction<Project> command = new ProtectedDecorator<Project>(new DeleteProjectAction(assignmentApi,
				tokenProviderApi, policyApi, projectid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	// @Override
	// public CollectionWrapper<Project> getProjectUsers(String projectid,
	// Boolean enabled, String name, int page, int perPage) {
	// ProtectedAction<List<User>> command = new
	// FilterProtectedDecorator<List<User>>(new PaginateDecorator<User>(
	// new GetProjectUsersAction(assignmentApi, tokenApi, policyApi, projectid,
	// name, enabled), page, perPage),
	// tokenApi, policyApi, parMap);
	//
	// List<User> ret = command.execute(getRequest());
	// return ret;
	// }

	public Project getMemberFromDriver(String projectid) {
		return assignmentApi.getProject(projectid);
	}
}
