package com.infinities.keystone4j.token.api.command;

import java.util.List;

import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokensForUserCommand extends AbstractTokenCommand<List<Token>> {

	private final String userid;
	private final String projectid;


	public DeleteTokensForUserCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String userid,
			String projectid) {
		super(tokenApi, trustApi, tokenDriver);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public List<Token> execute() {
		this.getTokenDriver().deleteTokensForUser(userid, projectid);

		List<Trust> trusts = this.getTrustApi().listTrustsForTrustee(userid);
		for (Trust trust : trusts) {
			this.getTokenDriver().deleteTokensForTrust(userid, trust.getId());
		}
		trusts = this.getTrustApi().listTrustsForTrustor(userid);
		for (Trust trust : trusts) {
			this.getTokenDriver().deleteTokensForTrust(trust.getTrustee().getId(), trust.getId());
		}

		return null;
	}
}
