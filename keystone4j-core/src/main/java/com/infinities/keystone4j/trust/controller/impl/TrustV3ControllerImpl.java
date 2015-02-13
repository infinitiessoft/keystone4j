package com.infinities.keystone4j.trust.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.ProtectedCollectionDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;
import com.infinities.keystone4j.trust.controller.action.CheckRoleForTrustAction;
import com.infinities.keystone4j.trust.controller.action.CreateTrustAction;
import com.infinities.keystone4j.trust.controller.action.DeleteTrustAction;
import com.infinities.keystone4j.trust.controller.action.GetRoleForTrustAction;
import com.infinities.keystone4j.trust.controller.action.GetTrustAction;
import com.infinities.keystone4j.trust.controller.action.ListRolesForTrustAction;
import com.infinities.keystone4j.trust.controller.action.ListTrustsAction;

public class TrustV3ControllerImpl extends BaseController implements TrustV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public TrustV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.assignmentApi.setIdentityApi(identityApi);
	}

	// TODO ignore validation.validated(schema.trust_create,'trust')
	@Override
	public MemberWrapper<Trust> createTrust(Trust trust) throws Exception {
		ProtectedAction<Trust> command = new ProtectedDecorator<Trust>(new CreateTrustAction(assignmentApi, identityApi,
				trustApi, tokenProviderApi, policyApi, trust), tokenProviderApi, policyApi);
		MemberWrapper<Trust> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Trust> listTrusts() throws Exception {
		FilterProtectedAction<Trust> command = new ProtectedCollectionDecorator<Trust>(new ListTrustsAction(assignmentApi,
				identityApi, trustApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public MemberWrapper<Trust> getTrust(String trustid) throws Exception {
		ProtectedAction<Trust> command = new GetTrustAction(assignmentApi, identityApi, trustApi, tokenProviderApi,
				policyApi, trustid);
		MemberWrapper<Trust> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteTrust(String trustid) throws Exception {
		ProtectedAction<Trust> command = new ProtectedDecorator<Trust>(new DeleteTrustAction(assignmentApi, identityApi,
				trustApi, tokenProviderApi, policyApi, trustid), tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Role> listRolesForTrust(String trustid) throws Exception {
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListRolesForTrustAction(
				assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi, trustid), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public void checkRoleForTrust(String trustid, String roleid) throws Exception {
		ProtectedAction<Trust> command = new ProtectedDecorator<Trust>(new CheckRoleForTrustAction(assignmentApi,
				identityApi, trustApi, tokenProviderApi, policyApi, trustid, roleid), tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public MemberWrapper<Role> getRoleForTrust(String trustid, String roleid) throws Exception {
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new GetRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenProviderApi, policyApi, trustid, roleid), tokenProviderApi, policyApi);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

}
