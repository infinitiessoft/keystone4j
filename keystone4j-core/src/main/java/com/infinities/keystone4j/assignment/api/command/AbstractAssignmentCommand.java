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
package com.infinities.keystone4j.assignment.api.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.Group;

public abstract class AbstractAssignmentCommand {

	private final CredentialApi credentialApi;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final AssignmentDriver assignmentDriver;
	private final RevokeApi revokeApi;


	public AbstractAssignmentCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver) {
		super();
		this.credentialApi = credentialApi;
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.revokeApi = revokeApi;
		this.assignmentDriver = assignmentDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public AssignmentDriver getAssignmentDriver() {
		return assignmentDriver;
	}

	public RevokeApi getRevokeApi() {
		return revokeApi;
	}

	protected List<String> getGroupIdsForUserId(String userid) throws Exception {
		List<String> ret = new ArrayList<String>();
		List<Group> groups = this.getIdentityApi().listGroupsForUser(userid, null);
		for (Group group : groups) {
			ret.add(group.getId());
		}
		return ret;
	}

	protected void filterProjectsList(List<Project> projectsList, String userid) throws Exception {
		List<Project> userProjects = this.getAssignmentApi().listProjectsForUser(userid, null);
		Set<String> userProjectsIds = new HashSet<String>();
		for (Project proj : userProjects) {
			userProjectsIds.add(proj.getId());
		}

		for (Iterator<Project> iterator = projectsList.iterator(); iterator.hasNext();) {
			Project project = iterator.next();
			if (!userProjectsIds.contains(project.getId())) {
				iterator.remove();
			}
		}
	}


	public static class Payload {

		String userid;
		String projectid;


		public Payload(String userid, String projectid) {
			super();
			this.userid = userid;
			this.projectid = projectid;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getProjectid() {
			return projectid;
		}

		public void setProjectid(String projectid) {
			this.projectid = projectid;
		}

	}

}
