package com.infinities.keystone4j.assignment.controller.action.role.v3;

import java.lang.reflect.Method;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.RoleWrapper;
import com.infinities.keystone4j.model.assignment.RolesWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractRoleAction extends AbstractAction<Role> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractDomainAction.class);
	protected AssignmentApi assignmentApi;
	protected Method getMemberFromDriver;


	public AbstractRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi) {
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
	protected CollectionWrapper<Role> getCollectionWrapper() {
		return new RolesWrapper();
	}

	@Override
	protected MemberWrapper<Role> getMemberWrapper() {
		return new RoleWrapper();
	}

	@Override
	public String getCollectionName() {
		return "roles";
	}

	@Override
	public String getMemberName() {
		return "role";
	}

}
