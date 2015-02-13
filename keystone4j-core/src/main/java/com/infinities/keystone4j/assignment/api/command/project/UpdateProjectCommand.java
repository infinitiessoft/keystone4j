package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateProjectCommand extends AbstractAssignmentCommand implements NotifiableCommand<Project> {

	private final String tenantid;
	private final Project tenant;


	public UpdateProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String tenantid, Project tenant) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.tenantid = tenantid;
		this.tenant = tenant;
	}

	@Override
	public Project execute() throws Exception {
		Project originalTenant = this.getAssignmentDriver().getProject(tenantid);

		String patentId = originalTenant.getParentId();
		if (!Strings.isNullOrEmpty(tenant.getParentId()) && !tenant.getParentId().equals(patentId)) {
			throw Exceptions.ForbiddenActionException.getInstance("Update of 'parent_id' is not allowed.");
		}

		// # NOTE(rodrigods): for the current implementation we only allow to
		// # disable a project if all projects below it in the hierarchy are
		// # already disabled. This also means that we can not enable a
		// # project that has disabled parents.

		Boolean originalTenantEnabled = originalTenant.getEnabled();
		Boolean tenantEnabled = tenant.getEnabled();
		if (!originalTenantEnabled && tenantEnabled) {
			assertAllParentsAreEnabled(tenantid);
		}

		if (originalTenantEnabled && !tenantEnabled) {
			assertWholeSubtreeIsDisabled(tenantid);
			getAssignmentApi().disableProject(tenantid);
		}

		Project ret = this.getAssignmentDriver().updateProject(tenantid, tenant);
		// TODO invalidate cache(getProject, getProjectByName)
		// self.get_project.invalidate(self, tenant_id)
		// self.get_project_by_name.invalidate(self, original_tenant['name'],
		// original_tenant['domain_id'])

		return ret;
	}

	private void assertWholeSubtreeIsDisabled(String projectid) throws Exception {
		List<Project> subtreeList = this.getAssignmentDriver().listProjectsInSubtree(projectid);
		for (Project ref : subtreeList) {
			if (ref.getEnabled()) {
				String msg = String.format("cannot disable project %s since its subtree contains enabled projects",
						projectid);
				throw Exceptions.ForbiddenActionException.getInstance(msg);
			}
		}
	}

	private void assertAllParentsAreEnabled(String projectid) throws Exception {
		List<Project> parentsList = this.getAssignmentApi().listProjectParents(projectid, null);
		for (Project project : parentsList) {
			if (!project.getEnabled()) {
				String msg = String.format("cannot enable project %s since it has disabled parents", projectid);
				throw Exceptions.ForbiddenActionException.getInstance(msg);
			}
		}

	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return tenantid;
		} else if (index == 2) {
			return tenant;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
