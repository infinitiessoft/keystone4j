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
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectParentsCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Project>> {

	private final String projectid;
	private final String userid;


	public ListProjectParentsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public List<Project> execute() throws Exception {
		List<Project> parents = this.getAssignmentDriver().listProjectParents(projectid);
		if (!Strings.isNullOrEmpty(userid)) {
			filterProjectsList(parents, userid);
		}
		return parents;
	}
}
