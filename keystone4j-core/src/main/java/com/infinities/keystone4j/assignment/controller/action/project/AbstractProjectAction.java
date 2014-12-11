package com.infinities.keystone4j.assignment.controller.action.project;

import java.lang.reflect.Method;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.ProjectWrapper;
import com.infinities.keystone4j.model.assignment.ProjectsWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractProjectAction extends AbstractAction<Project> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractDomainAction.class);
	protected AssignmentApi assignmentApi;
	protected Method getMemberFromDriver;


	public AbstractProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi) {
		super(tokenProviderApi);
		this.assignmentApi = assignmentApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	protected CollectionWrapper<Project> getCollectionWrapper() {
		return new ProjectsWrapper();
	}

	@Override
	protected MemberWrapper<Project> getMemberWrapper() {
		return new ProjectWrapper();
	}

	@Override
	public String getCollectionName() {
		return "projects";
	}

	@Override
	public String getMemberName() {
		return "project";
	}

}
