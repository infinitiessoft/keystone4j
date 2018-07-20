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
package com.infinities.keystone4j.assignment.api.command.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteDomainCommand extends AbstractAssignmentCommand implements NotifiableCommand<Domain> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteDomainCommand.class);
	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final static String DELETE_DEFAULT_DOMAIN = "delete the default domain";
	private final static String DOMAIN_IS_ENABLED = "delete a domain that is not disabled";
	private final String domainid;


	public DeleteDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainid = domainid;
	}

	@Override
	public Domain execute() throws Exception {
		String defaultDomainid = Config.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText();

		if (defaultDomainid.equals(domainid)) {
			throw Exceptions.ForbiddenActionException.getInstance(null, DELETE_DEFAULT_DOMAIN);
		}

		Domain domain = this.getAssignmentDriver().getDomain(domainid);

		// avoid inadvertent deletes.
		if (domain.getEnabled()) {
			throw Exceptions.ForbiddenActionException.getInstance(null, DOMAIN_IS_ENABLED);
		}

		deleteDomainContents(domainid);
		this.getAssignmentDriver().deleteDomain(domainid);
		// TODO invalidate cache getDomain, getDomainByName

		return null;
	}

	private void deleteDomainContents(String domainid) throws Exception {
		// cacade remove project,group,user
		// hibernate handle this automately
		List<User> userRefs = this.getIdentityApi().listUsers(domainid, null);
		List<Project> projRefs = this.getAssignmentApi().listProjectsInDomain(domainid);
		List<Group> groupRefs = this.getIdentityApi().listGroups(domainid, null);

		List<Project> roots = new ArrayList<Project>();
		for (Project ref : projRefs) {
			if (Strings.isNullOrEmpty(ref.getParentId())) {
				roots.add(ref);
			}
		}
		Set<String> examined = new HashSet<String>();
		for (Project project : roots) {
			deleteProjects(project, projRefs, examined);
		}

		for (Group group : groupRefs) {
			if (domainid.equals(group.getDomainid())) {
				try {
					this.getIdentityApi().deleteGroup(group.getId());
				} catch (Exception e) {
					logger.debug("Group {} not found when deleting domain contents for {}, continuing with cleanup.",
							new Object[] { group.getId(), domainid });
				}
			}
		}

		for (User user : userRefs) {
			if (domainid.equals(user.getDomainId())) {
				try {
					this.getIdentityApi().deleteUser(user.getId());
				} catch (Exception e) {
					logger.debug("User {} not found when deleting domain contents for {}, continuing with cleanup.",
							new Object[] { user.getId(), domainid });
				}
			}
		}

	}

	private void deleteProjects(Project project, List<Project> projects, Set<String> examined) {
		if (examined.contains(project.getId())) {
			String msg = String.format("Circular reference or a repeated entry found projects hierarchy - %s",
					project.getId());
			logger.error(msg);
			return;
		}
		examined.add(project.getId());
		List<Project> children = new ArrayList<Project>();
		for (Project proj : projects) {
			if (proj.getParentId().equals(project.getId())) {
				children.add(proj);
			}
		}

		for (Project proj : children) {
			deleteProjects(proj, projects, examined);
		}

		try {
			this.getAssignmentApi().deleteProject(project.getId());
		} catch (Exception e) {
			logger.debug("Project {} not found when deleting domain contents for {}, continuing with cleanup.",
					new Object[] { project.getId(), domainid });
		}

	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return domainid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
