package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.controller.action.grant.CheckGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CheckGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CheckGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CheckGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.DeleteGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.DeleteGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.DeleteGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.DeleteGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.ListGrantsByGroupDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.ListGrantsByGroupProjectAction;
import com.infinities.keystone4j.assignment.controller.action.grant.ListGrantsByUserDomainAction;
import com.infinities.keystone4j.assignment.controller.action.grant.ListGrantsByUserProjectAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.CreateRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.DeleteRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.GetRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.ListRolesAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.UpdateRoleAction;
import com.infinities.keystone4j.assignment.controller.callback.CheckGrantCollectionProtectionCallback;
import com.infinities.keystone4j.assignment.controller.callback.CheckGrantProtectionCallback;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedCollectionDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.RoleV3 20141209

public class RoleV3ControllerImpl extends BaseController implements RoleV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	// private static final boolean INHERITED = false;

	public RoleV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<Role> createRole(Role role) throws Exception {
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateRoleAction(assignmentApi, tokenProviderApi,
				policyApi, role), tokenProviderApi, policyApi);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Role> listRoles() throws Exception {
		FilterProtectedAction<Role> command = new FilterProtectedDecorator<Role>(new ListRolesAction(assignmentApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Role> ret = command.execute(getRequest(), "name");
		return ret;
	}

	@Override
	public MemberWrapper<Role> getRole(String roleid) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new GetRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Role> updateRole(String roleid, Role role) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new UpdateRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid, role), tokenProviderApi, policyApi, ref);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteRole(String roleid) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	@Override
	public void createGrantByUserDomain(String roleid, String userid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, null, domainid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateGrantByUserDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void createGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, null, domainid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateGrantByGroupDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Role> listGrantsByUserDomain(String userid, String domainid) throws Exception {
		CheckGrantCollectionProtectionCallback callback = new CheckGrantCollectionProtectionCallback(tokenProviderApi,
				policyApi, identityApi, assignmentApi, userid, null, null, null, domainid);
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListGrantsByUserDomainAction(
				assignmentApi, identityApi, tokenProviderApi, policyApi, userid, domainid), callback, tokenProviderApi,
				policyApi);
		return command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Role> listGrantsByGroupDomain(String groupid, String domainid) throws Exception {
		CheckGrantCollectionProtectionCallback callback = new CheckGrantCollectionProtectionCallback(tokenProviderApi,
				policyApi, identityApi, assignmentApi, null, null, groupid, null, domainid);
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListGrantsByGroupDomainAction(
				assignmentApi, identityApi, tokenProviderApi, policyApi, groupid, domainid), callback, tokenProviderApi,
				policyApi);
		return command.execute(getRequest());
	}

	@Override
	public void checkGrantByUserDomain(String roleid, String userid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, null, domainid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckGrantByUserDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void checkGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, null, domainid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckGrantByGroupDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void revokeGrantByUserDomain(String roleid, String userid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, null, domainid, true);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteGrantByUserDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void revokeGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, null, domainid, true);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteGrantByGroupDomainAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, domainid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void createGrantByUserProject(String roleid, String userid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, projectid, null);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateGrantByUserProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void createGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, projectid, null);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateGrantByGroupProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Role> listGrantsByUserProject(String userid, String projectid) throws Exception {
		CheckGrantCollectionProtectionCallback callback = new CheckGrantCollectionProtectionCallback(tokenProviderApi,
				policyApi, identityApi, assignmentApi, userid, null, null, projectid, null);
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListGrantsByUserProjectAction(
				assignmentApi, identityApi, tokenProviderApi, policyApi, userid, projectid), callback, tokenProviderApi,
				policyApi);
		return command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Role> listGrantsByGroupProject(String groupid, String projectid) throws Exception {
		CheckGrantCollectionProtectionCallback callback = new CheckGrantCollectionProtectionCallback(tokenProviderApi,
				policyApi, identityApi, assignmentApi, null, null, groupid, projectid, null);
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListGrantsByGroupProjectAction(
				assignmentApi, identityApi, tokenProviderApi, policyApi, groupid, projectid), callback, tokenProviderApi,
				policyApi);
		return command.execute(getRequest());
	}

	@Override
	public void checkGrantByUserProject(String roleid, String userid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, projectid, null);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckGrantByUserProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void checkGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, projectid, null);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckGrantByGroupProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void revokeGrantByUserProject(String roleid, String userid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, userid, roleid, null, projectid, null, true);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteGrantByUserProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, userid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void revokeGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, null, roleid, groupid, projectid, null, true);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteGrantByGroupProjectAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, roleid, groupid, projectid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	public Role getMemberFromDriver(String roleid) {
		return assignmentApi.getRole(roleid);
	}

}
