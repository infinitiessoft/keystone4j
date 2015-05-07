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

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	private final String projectid;


	public GetProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String projectid) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext context) throws Exception {
		Project ref = this.getAssignmentApi().getProject(projectid);
		expandProjectRef(context, ref);
		return this.wrapMember(context, ref);
	}

	private void expandProjectRef(ContainerRequestContext context, Project ref) throws Exception {
		String userid = getAuthContext(context).getUserId();
		if (context.getUriInfo().getQueryParameters().containsKey("parents_as_list")
				&& queryFilterIsTrue(context.getUriInfo().getQueryParameters().getFirst("parents_as_list"))) {
			List<Project> parents = assignmentApi.listProjectParents(ref.getId(), userid);
			for (Project p : parents) {
				ref.getParents().add(wrapMember(context, p));
			}
		}

		if (context.getUriInfo().getQueryParameters().containsKey("subtree_as_list")
				&& queryFilterIsTrue(context.getUriInfo().getQueryParameters().getFirst("subtree_as_list"))) {
			List<Project> subtree = assignmentApi.listProjectsInSubtree(ref.getId(), userid);
			for (Project p : subtree) {
				ref.getSubtree().add(wrapMember(context, p));
			}
		}
	}

	@Override
	public String getName() {
		return "get_project";
	}
}
