package com.infinities.keystone4j.assignment.command.domain;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateDomainCommand extends AbstractAssignmentCommand<Domain> {

	private final String domainid;
	private final Domain domain;


	public UpdateDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String domainid, Domain domain) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.domainid = domainid;
		this.domain = domain;
	}

	@Override
	public Domain execute() {
		Domain ret = this.getAssignmentDriver().updateDomain(domainid, domain);
		if (!domain.getEnabled()) {
			List<Project> projects = this.getAssignmentApi().listProjects();

			for (Project project : projects) {
				if (domainid.equals(project.getDomain().getId())) {
					List<User> users = this.getAssignmentApi().listUsersForProject(project.getId());
					for (User user : users) {
						this.getTokenApi().deleteTokensForUser(user.getId(), project.getId());
					}
				}
			}
		}
		// invalidate cache(getDomain, getDomainByName)

		return ret;
	}
}
