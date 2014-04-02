package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class CheckV3TokenCommand extends AbstractTokenProviderCommand<TokenMetadata> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(CheckV3TokenCommand.class);
	private final String tokenid;


	public CheckV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenMetadata execute() {
		// String uniqueid = null;
		// try {
		// uniqueid = Cms.Instance.hashToken(tokenid);
		// } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
		// DecoderException e) {
		// logger.error("unexpected error", e);
		// throw Exceptions.UnexpectedException.getInstance(null);
		// }
		// logger.debug("check token uniqueid: {}", uniqueid);
		this.getTokenProviderApi().validateV3Token(tokenid);
		return null;
	}

}
