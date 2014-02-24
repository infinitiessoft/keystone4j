package com.infinities.keystone4j.token.command;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.model.Trust;

public class DeleteTokensForUserCommand extends AbstractTokenCommand<List<Token>> {

	private final String userid;
	private final String projectid;


	public DeleteTokensForUserCommand(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, String userid,
			String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public List<Token> execute() {
		this.getTokenDriver().deleteTokensForUser(userid, projectid);
		List<Trust> trusts = this.getTrustApi().listTrustsForTrustee(userid);
		for (Trust trust : trusts) {
			this.getTokenDriver().deleteTokensForTrust(userid, projectid, trust.getId());
		}

		trusts = this.getTrustApi().listTrustsForTrustor(userid);
		for (Trust trust : trusts) {
			this.getTokenDriver().deleteTokensForTrust(trust.getTrustee().getId(), projectid, trust.getId());
		}

		return null;
	}

}
