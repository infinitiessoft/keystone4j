//package com.infinities.keystone4j.token.provider.api.command;
//
//import com.infinities.keystone4j.model.auth.TokenIdAndData;
//import com.infinities.keystone4j.token.provider.TokenProviderApi;
//import com.infinities.keystone4j.token.provider.TokenProviderDriver;
//
//public class CheckV3TokenCommand extends AbstractTokenProviderCommand<TokenIdAndData> {
//
//	// private final static Logger logger =
//	// LoggerFactory.getLogger(CheckV3TokenCommand.class);
//	private final String tokenid;
//
//
//	public CheckV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
//		super(tokenProviderApi, tokenProviderDriver);
//		this.tokenid = tokenid;
//	}
//
//	@Override
//	public TokenIdAndData execute() {
//		String uniqueid = this.getUniqueId(tokenid);
//		this.getTokenProviderApi().validateV3Token(uniqueid);
//		return null;
//	}
//
// }
