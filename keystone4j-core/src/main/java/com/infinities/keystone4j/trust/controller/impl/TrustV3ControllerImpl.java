package com.infinities.keystone4j.trust.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
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
		ProtectedAction<Trust> command = new ProtectedDecorator<Trust>(new CreateTrustAction(assignmentApi, identityApi, trustApi,
				tokenApi, trust), null, tokenApi, policyApi, parMap);
		Trust ret = command.execute(getRequest());
		return new TrustWrapper(ret, getRequest());
	}

	@Override
	public TrustsWrapper listTrusts(String trustorid, String trusteeid, int page, int perPage) {
		parMap.put("trustorid", trustorid);
		parMap.put("trusteeid", trusteeid);
		ProtectedAction<List<Trust>> command = new ProtectedDecorator<List<Trust>>(new PaginateDecorator<Trust>(
				new ListTrustsAction(assignmentApi, identityApi, trustApi, tokenApi, policyApi, trustorid, trusteeid), page,
				perPage), null, tokenApi, policyApi, parMap);

		List<Trust> ret = command.execute(getRequest());
		return new TrustsWrapper(ret, getRequest());
	}

	@Override
	public TrustWrapper getTrust(String trustid) {
		parMap.put("trustid", trustid);
		ProtectedAction<Trust> command = new GetTrustAction(assignmentApi, identityApi, trustApi, tokenApi, trustid);
		Trust ret = command.execute(getRequest());
		return new TrustWrapper(ret, getRequest());
	}

	@Override
	public void deleteTrust(String trustid) {
		parMap.put("trustid", trustid);
		ProtectedAction<Trust> command = new ProtectedDecorator<Trust>(new DeleteTrustAction(assignmentApi, identityApi, trustApi,
				tokenApi, trustid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public RolesWrapper listRolesForTrust(String trustid, int page, int perPage) {
		parMap.put("trustid", trustid);
		ProtectedAction<List<Role>> command = new ProtectedDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListRolesForTrustAction(assignmentApi, identityApi, trustApi, tokenApi, trustid), page, perPage), null,
				tokenApi, policyApi, parMap);

		List<Role> ret = command.execute(getRequest());
		return new RolesWrapper(ret, getRequest());
	}

	@Override
	public void checkRoleForTrust(String trustid, String roleid) {
		parMap.put("trustid", trustid);
		parMap.put("roleid", roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid), null, tokenApi, policyApi, parMap);

		command.execute(getRequest());
	}

	@Override
	public RoleWrapper getRoleForTrust(String trustid, String roleid) {
		parMap.put("trustid", trustid);
		parMap.put("roleid", roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new GetRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid), null, tokenApi, policyApi, parMap);
		Role ret = command.execute(getRequest());
		return new RoleWrapper(ret, getRequest().getUriInfo().getBaseUri().toASCIIString() + "v3/roles/");
	}

}
