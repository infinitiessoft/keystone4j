package com.infinities.keystone4j.token.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.auth.RevokedWrapper;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.wrapper.SignedWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.JsonUtils;

public class GetRevocationListAction extends AbstractTokenAction implements ProtectedAction<String> {

	private final static Logger logger = LoggerFactory.getLogger(GetRevocationListAction.class);


	public GetRevocationListAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, trustApi, policyApi);
	}

	@Override
	public MemberWrapper<String> execute(ContainerRequestContext request) throws Exception {
		if (!Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			throw Exceptions.Gone.getInstance();
		}

		List<Token> tokens = tokenProviderApi.listRevokedTokens();
		RevokedWrapper data = new RevokedWrapper();
		data.setRevoked(tokens);

		String certfile = Config.Instance.getOpt(Config.Type.signing, "certfile").asText();
		String keyfile = Config.Instance.getOpt(Config.Type.signing, "keyfile").asText();
		String jsonData = JsonUtils.toJson(data);
		String signedText = Cms.Instance.signText(jsonData, certfile, keyfile);
		logger.debug("signed text: {}", signedText);
		SignedWrapper wrapper = new SignedWrapper();
		wrapper.setSigned(signedText);
		return wrapper;
	}

	@Override
	public String getName() {
		return "revocation_list";
	}

	@Override
	public String getCollectionName() {
		return null;
	}

	@Override
	public String getMemberName() {
		return null;
	}

	@Override
	public MemberWrapper<String> getMemberWrapper() {
		return new SignedWrapper();
	}
}
