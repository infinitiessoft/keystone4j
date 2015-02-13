package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteProjectCommand extends AbstractAssignmentCommand implements NotifiableCommand<Project> {

	private final String tenantid;


	public DeleteProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String tenantid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.tenantid = tenantid;
	}

	@Override
	public Project execute() throws Exception {
		if (!this.getAssignmentDriver().isLeafProject(tenantid)) {
			String msg = String.format("cannot delete the project %s since it is not a leaf in the hierarchy", tenantid);
			throw Exceptions.ForbiddenActionException.getInstance(msg);
		}

		// verify project exist
		this.getAssignmentDriver().getProject(tenantid);
		List<String> projectUserIds = this.getAssignmentApi().listUserIdsForProject(tenantid);
		for (String userid : projectUserIds) {
			Payload payload = new Payload(userid, tenantid);
			this.getAssignmentApi().emitInvalidateUserProjectTokensNotification(payload);
		}

		Project ret = this.getAssignmentDriver().deleteProject(tenantid);
		// TODO invalidate cache(getProject, getProjectByName)
		// self.get_project.invalidate(self, tenant_id)
		// self.get_project_by_name.invalidate(self, project['name'],
		// project['domain_id'])
		this.getCredentialApi().deleteCredentialsForProject(tenantid);

		return ret;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return tenantid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
