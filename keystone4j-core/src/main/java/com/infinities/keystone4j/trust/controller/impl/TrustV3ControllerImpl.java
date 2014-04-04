package com.infinities.keystone4j.trust.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.RoleWrapper;
import com.infinities.keystone4j.model.assignment.RolesWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustWrapper;
import com.infinities.keystone4j.model.trust.TrustsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.action.CheckRoleForTrustAction;
import com.infinities.keystone4j.trust.action.CreateTrustAction;
import com.infinities.keystone4j.trust.action.DeleteTrustAction;
import com.infinities.keystone4j.trust.action.GetRoleForTrustAction;
import com.infinities.keystone4j.trust.action.GetTrustAction;
import com.infinities.keystone4j.trust.action.ListRolesForTrustAction;
import com.infinities.keystone4j.trust.action.ListTrustsAction;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;

public class TrustV3ControllerImpl extends BaseController implements TrustV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public TrustV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public TrustWrapper createTrust(Trust trust) {
		parMap.put("trust", trust);
		Action<Trust> command = new PolicyCheckDecorator<Trust>(new CreateTrustAction(assignmentApi, identityApi, trustApi,
				tokenApi, trust), null, tokenApi, policyApi, parMap);
		Trust ret = command.execute(getRequest());
		return new TrustWrapper(ret, getRequest());
	}

	@Override
	public TrustsWrapper listTrusts(String trustorid, String trusteeid, int page, int perPage) {
		parMap.put("trustorid", trustorid);
		parMap.put("trusteeid", trusteeid);
		Action<List<Trust>> command = new PolicyCheckDecorator<List<Trust>>(new PaginateDecorator<Trust>(
				new ListTrustsAction(assignmentApi, identityApi, trustApi, tokenApi, policyApi, trustorid, trusteeid), page,
				perPage), null, tokenApi, policyApi, parMap);

		List<Trust> ret = command.execute(getRequest());
		return new TrustsWrapper(ret, getRequest());
	}

	@Override
	public TrustWrapper getTrust(String trustid) {
		parMap.put("trustid", trustid);
		Action<Trust> command = new GetTrustAction(assignmentApi, identityApi, trustApi, tokenApi, trustid);
		Trust ret = command.execute(getRequest());
		return new TrustWrapper(ret, getRequest());
	}

	@Override
	public void deleteTrust(String trustid) {
		parMap.put("trustid", trustid);
		Action<Trust> command = new PolicyCheckDecorator<Trust>(new DeleteTrustAction(assignmentApi, identityApi, trustApi,
				tokenApi, trustid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public RolesWrapper listRolesForTrust(String trustid, int page, int perPage) {
		parMap.put("trustid", trustid);
		Action<List<Role>> command = new PolicyCheckDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListRolesForTrustAction(assignmentApi, identityApi, trustApi, tokenApi, trustid), page, perPage), null,
				tokenApi, policyApi, parMap);

		List<Role> ret = command.execute(getRequest());
		return new RolesWrapper(ret, getRequest());
	}

	@Override
	public void checkRoleForTrust(String trustid, String roleid) {
		parMap.put("trustid", trustid);
		parMap.put("roleid", roleid);
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid), null, tokenApi, policyApi, parMap);

		command.execute(getRequest());
	}

	@Override
	public RoleWrapper getRoleForTrust(String trustid, String roleid) {
		parMap.put("trustid", trustid);
		parMap.put("roleid", roleid);
		Action<Role> command = new PolicyCheckDecorator<Role>(new GetRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid), null, tokenApi, policyApi, parMap);
		Role ret = command.execute(getRequest());
		return new RoleWrapper(ret, getRequest().getUriInfo().getBaseUri().toASCIIString() + "v3/roles/");
	}

}
