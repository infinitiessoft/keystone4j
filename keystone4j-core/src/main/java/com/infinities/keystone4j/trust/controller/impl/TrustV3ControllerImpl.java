package com.infinities.keystone4j.trust.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
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
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustWrapper;
import com.infinities.keystone4j.trust.model.TrustsWrapper;

public class TrustV3ControllerImpl implements TrustV3Controller {

	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private TrustApi trustApi;
	private TokenApi tokenApi;


	public TrustV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenApi = tokenApi;
	}

	@Override
	public TrustWrapper createTrust(Trust trust) {
		Action<Trust> command = new PolicyCheckDecorator<Trust>(new CreateTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trust));
		Trust ret = command.execute();
		return new TrustWrapper(ret);
	}

	@Override
	public TrustsWrapper listTrusts(String trustorid, String trusteeid, int page, int perPage) {
		Action<List<Trust>> command = new FilterCheckDecorator<List<Trust>>(new PaginateDecorator<Trust>(
				new ListTrustsAction(assignmentApi, identityApi, trustApi, tokenApi, trustorid, trusteeid), page, perPage));

		List<Trust> ret = command.execute();
		return new TrustsWrapper(ret);
	}

	@Override
	public TrustWrapper getTrust(String trustid) {
		Action<Trust> command = new PolicyCheckDecorator<Trust>(new GetTrustAction(assignmentApi, identityApi, trustApi,
				tokenApi, trustid));
		Trust ret = command.execute();
		return new TrustWrapper(ret);
	}

	@Override
	public void deleteTrust(String trustid) {
		Action<Trust> command = new PolicyCheckDecorator<Trust>(new DeleteTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid));
		command.execute();
	}

	@Override
	public RolesWrapper listRolesForTrust(String trustid, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListRolesForTrustAction(assignmentApi, identityApi, trustApi, tokenApi, trustid), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public void checkRoleForTrust(String trustid, String roleid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid));

		command.execute();
	}

	@Override
	public RoleWrapper getRoleForTrust(String trustid, String roleid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new GetRoleForTrustAction(assignmentApi, identityApi,
				trustApi, tokenApi, trustid, roleid));
		Role ret = command.execute();
		return new RoleWrapper(ret);
	}

}
