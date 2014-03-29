package com.infinities.keystone4j.token.command;

import java.util.List;

import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokensForDomainCommand extends AbstractTokenCommand<List<Token>> {

	// private final String domainid;

	public DeleteTokensForDomainCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String domainid) {
		// this.domainid = domainid;
		super(tokenApi, trustApi, tokenDriver);
	}

	@Override
	public List<Token> execute() {
		// List<Project> projects = this.getAssignmentApi().listProjects();
		//
		// for (Project project : projects) {
		// if (domainid.equals(project.getDomain().getId())) {
		// List<User> users =
		// this.getAssignmentApi().listUsersForProject(project.getId());
		// for (User user : users) {
		// this.getTokenApi().deleteTokensForUser(user.getId(),
		// project.getId());
		// }
		// }
		// }

		return null;
	}
}
