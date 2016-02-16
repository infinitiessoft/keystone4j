/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateProjectCommand extends AbstractAssignmentCommand implements NotifiableCommand<Project> {

	private final String tenantid;
	private final Project tenant;


	public CreateProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String tenantid, Project tenant) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.tenantid = tenantid;
		this.tenant = tenant;
	}

	@Override
	public Project execute() throws Exception {

		if (!Strings.isNullOrEmpty(tenant.getParentId())) {
			Project parentRef = this.getAssignmentApi().getProject(tenant.getParentId());
			List<Project> parentsList = this.getAssignmentApi().listProjectParents(parentRef.getId(), null);
			parentsList.add(parentRef);
			for (Project ref : parentsList) {
				if (!ref.getDomainId().equals(tenant.getDomainId())) {
					throw Exceptions.ForbiddenActionException
							.getInstance("cannot create project within a different domain than its parent.");
				}
				if (!ref.getEnabled()) {
					throw Exceptions.ForbiddenActionException.getInstance(String.format(
							"cannot create project in a branch containing a disabled project %s.", ref.getId()));
				}
			}
			assertMaxHierarchyDepth(tenant.getParentId(), parentsList);
		}
		Project ret = this.getAssignmentDriver().createProject(tenantid, tenant);
		// TODO ignore if SHOULD_CACHE(ret):

		return ret;
	}

	private void assertMaxHierarchyDepth(String projectid, List<Project> parentsList) throws Exception {
		if (parentsList == null) {
			parentsList = this.getAssignmentApi().listProjectParents(projectid, null);
		}
		int maxDepth = Config.getOpt(Config.Type.DEFAULT, "max_project_tree_depth").asInteger();
		if (getHierarchyDepth(parentsList) > maxDepth) {
			throw Exceptions.ForbiddenActionException.getInstance(String.format(
					"max hierarchy depth reached for %s branch.", projectid));

		}
	}

	private int getHierarchyDepth(List<Project> parentsList) {
		return parentsList.size() + 1;
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
