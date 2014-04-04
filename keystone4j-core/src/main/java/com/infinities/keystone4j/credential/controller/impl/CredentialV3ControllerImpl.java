package com.infinities.keystone4j.credential.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.action.CreateCredentialAction;
import com.infinities.keystone4j.credential.action.DeleteCredentialAction;
import com.infinities.keystone4j.credential.action.GetCredentialAction;
import com.infinities.keystone4j.credential.action.ListCredentialsAction;
import com.infinities.keystone4j.credential.action.UpdateCredentialAction;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.credential.CredentialWrapper;
import com.infinities.keystone4j.model.credential.CredentialsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class CredentialV3ControllerImpl extends BaseController implements CredentialV3Controller {

	private final CredentialApi credentialApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public CredentialV3ControllerImpl(CredentialApi credentialApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.credentialApi = credentialApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public CredentialWrapper createCredential(Credential credential) {
		parMap.put("credential", credential);
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new CreateCredentialAction(credentialApi,
				credential), null, tokenApi, policyApi, parMap);
		Credential ret = command.execute(getRequest());
		return new CredentialWrapper(ret);
	}

	@Override
	public CredentialsWrapper listCredentials(int page, int perPage) {
		Action<List<Credential>> command = new PolicyCheckDecorator<List<Credential>>(new PaginateDecorator<Credential>(
				new ListCredentialsAction(credentialApi), page, perPage), null, tokenApi, policyApi, parMap);

		List<Credential> ret = command.execute(getRequest());
		return new CredentialsWrapper(ret);
	}

	@Override
	public CredentialWrapper getCredential(String credentialid) {
		parMap.put("credentialid", credentialid);
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new GetCredentialAction(credentialApi,
				credentialid), null, tokenApi, policyApi, parMap);
		Credential ret = command.execute(getRequest());
		return new CredentialWrapper(ret);
	}

	@Override
	public CredentialWrapper updateCredential(String credentialid, Credential credential) {
		parMap.put("credentialid", credentialid);
		parMap.put("credential", credential);
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new UpdateCredentialAction(credentialApi,
				credentialid, credential), null, tokenApi, policyApi, parMap);
		Credential ret = command.execute(getRequest());
		return new CredentialWrapper(ret);
	}

	@Override
	public void deleteCredential(String credentialid) {
		parMap.put("credentialid", credentialid);
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new DeleteCredentialAction(credentialApi,
				credentialid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
