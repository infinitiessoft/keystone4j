package com.infinities.keystone4j.token.command;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokensForDomainCommand extends AbstractTokenCommand<List<Token>> {

	private final String domainid;


	public DeleteTokensForDomainCommand(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, String domainid) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
		this.domainid = domainid;
	}

	@Override
	public List<Token> execute() {
		List<Project> projects = this.getAssignmentApi().listProjects();

		for (Project project : projects) {
			if (domainid.equals(project.getDomain().getId())) {
				List<User> users = this.getAssignmentApi().listUsersForProject(project.getId());
				for (User user : users) {
					this.getTokenApi().deleteTokensForUser(user.getId(), project.getId());
				}
			}
		}

		return null;
	}
}
