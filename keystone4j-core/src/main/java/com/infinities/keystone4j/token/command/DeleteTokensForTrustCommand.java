package com.infinities.keystone4j.token.command;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokensForTrustCommand extends AbstractTokenCommand<List<Token>> {

	private final String userid;
	private final String trustid;


	public DeleteTokensForTrustCommand(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, String userid,
			String trustid) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
		this.userid = userid;
		this.trustid = trustid;
	}

	@Override
	public List<Token> execute() {
		// List<Token> tokens = this.getTokenDriver().listTokensForTrust(userid,
		// trustid);
		this.getTokenDriver().deleteTokensForTrust(userid, null, trustid);

		// for (Token token : tokens) {
		// String uniqueid = new Cms().uniqueid(token.getId());
		// // invalidate individual token cache
		// }

		// invalidate_revocation_list
		return null;
	}

}
