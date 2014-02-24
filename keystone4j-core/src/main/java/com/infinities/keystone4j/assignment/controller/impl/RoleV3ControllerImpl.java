package com.infinities.keystone4j.assignment.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.action.grant.CheckGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.action.grant.CheckGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.action.grant.CheckGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.action.grant.CheckGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.action.grant.CreateGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.action.grant.CreateGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.action.grant.CreateGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.action.grant.CreateGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.action.grant.DeleteGrantByGroupDomainAction;
import com.infinities.keystone4j.assignment.action.grant.DeleteGrantByGroupProjectAction;
import com.infinities.keystone4j.assignment.action.grant.DeleteGrantByUserDomainAction;
import com.infinities.keystone4j.assignment.action.grant.DeleteGrantByUserProjectAction;
import com.infinities.keystone4j.assignment.action.grant.ListGrantsByGroupDomainAction;
import com.infinities.keystone4j.assignment.action.grant.ListGrantsByGroupProjectAction;
import com.infinities.keystone4j.assignment.action.grant.ListGrantsByUserDomainAction;
import com.infinities.keystone4j.assignment.action.grant.ListGrantsByUserProjectAction;
import com.infinities.keystone4j.assignment.action.role.v3.CreateRoleAction;
import com.infinities.keystone4j.assignment.action.role.v3.DeleteRoleAction;
import com.infinities.keystone4j.assignment.action.role.v3.GetRoleAction;
import com.infinities.keystone4j.assignment.action.role.v3.ListRolesAction;
import com.infinities.keystone4j.assignment.action.role.v3.UpdateRoleAction;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;

public class RoleV3ControllerImpl implements RoleV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private static final boolean INHERITED = false;


	public RoleV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
	}

	@Override
	public RoleWrapper createRole(Role role) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CreateRoleAction(assignmentApi, role));
		Role ret = command.execute();
		return new RoleWrapper(ret);
	}

	@Override
	public RolesWrapper listRoles(String name, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(new PaginateDecorator<Role>(new ListRolesAction(
				assignmentApi, name), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public RoleWrapper getRole(String roleid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new GetRoleAction(assignmentApi, roleid));
		Role ret = command.execute();
		return new RoleWrapper(ret);
	}

	@Override
	public RoleWrapper updateRole(String roleid, Role role) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new UpdateRoleAction(assignmentApi, roleid, role));
		Role ret = command.execute();
		return new RoleWrapper(ret);
	}

	@Override
	public void deleteRole(String roleid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new DeleteRoleAction(assignmentApi, roleid));
		command.execute();
	}

	@Override
	public void createGrantByUserDomain(String roleid, String userid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CreateGrantByUserDomainAction(assignmentApi, identityApi,
				roleid, userid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public void createGrantByGroupDomain(String roleid, String groupid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CreateGrantByGroupDomainAction(assignmentApi, identityApi,
				roleid, groupid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public RolesWrapper listGrantsByUserDomain(String userid, String domainid, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListGrantsByUserDomainAction(assignmentApi, identityApi, userid, domainid, INHERITED), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public RolesWrapper listGrantsByGroupDomain(String groupid, String domainid, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListGrantsByGroupDomainAction(assignmentApi, identityApi, groupid, domainid, INHERITED), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public void checkGrantByUserDomain(String roleid, String userid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckGrantByUserDomainAction(assignmentApi, identityApi,
				roleid, userid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public void checkGrantByGroupDomain(String roleid, String groupid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckGrantByGroupDomainAction(assignmentApi, identityApi,
				roleid, groupid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public void revokeGrantByUserDomain(String roleid, String userid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new DeleteGrantByUserDomainAction(assignmentApi, identityApi,
				roleid, userid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public void revokeGrantByGroupDomain(String roleid, String groupid, String domainid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new DeleteGrantByGroupDomainAction(assignmentApi, identityApi,
				roleid, groupid, domainid, INHERITED));
		command.execute();
	}

	@Override
	public void createGrantByUserProject(String roleid, String userid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CreateGrantByUserProjectAction(assignmentApi, identityApi,
				roleid, userid, projectid));
		command.execute();
	}

	@Override
	public void createGrantByGroupProject(String roleid, String groupid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CreateGrantByGroupProjectAction(assignmentApi,
				identityApi, roleid, groupid, projectid));
		command.execute();
	}

	@Override
	public RolesWrapper listGrantsByUserProject(String userid, String projectid, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(new PaginateDecorator<Role>(
				new ListGrantsByUserProjectAction(assignmentApi, identityApi, userid, projectid, INHERITED), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public RolesWrapper listGrantsByGroupProject(String groupid, String projectid, int page, int perPage) {
		Action<List<Role>> command = new FilterCheckDecorator<List<Role>>(
				new PaginateDecorator<Role>(new ListGrantsByGroupProjectAction(assignmentApi, identityApi, groupid,
						projectid, INHERITED), page, perPage));

		List<Role> ret = command.execute();
		return new RolesWrapper(ret);
	}

	@Override
	public void checkGrantByUserProject(String roleid, String userid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckGrantByUserProjectAction(assignmentApi, identityApi,
				roleid, userid, projectid, INHERITED));
		command.execute();
	}

	@Override
	public void checkGrantByGroupProject(String roleid, String groupid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new CheckGrantByGroupProjectAction(assignmentApi, identityApi,
				roleid, groupid, projectid, INHERITED));
		command.execute();
	}

	@Override
	public void revokeGrantByUserProject(String roleid, String userid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new DeleteGrantByUserProjectAction(assignmentApi, identityApi,
				roleid, userid, projectid, INHERITED));
		command.execute();
	}

	@Override
	public void revokeGrantByGroupProject(String roleid, String groupid, String projectid) {
		Action<Role> command = new PolicyCheckDecorator<Role>(new DeleteGrantByGroupProjectAction(assignmentApi,
				identityApi, roleid, groupid, projectid, INHERITED));
		command.execute();
	}

}
