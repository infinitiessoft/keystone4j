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
package com.infinities.keystone4j.assignment.controller.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	private final String projectid;
	private final Project project;


	public UpdateProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String projectid, Project project) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.project = project;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext context) throws Exception {
		requireMatchingId(projectid, project);
		requireMatchingDomainId(project, assignmentApi.getProject(projectid));
		Project ref = this.getAssignmentApi().updateProject(projectid, project);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_project";
	}
}
