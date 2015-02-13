package com.infinities.keystone4j.assignment.api;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.api.command.decorator.ResponseTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand.Payload;
import com.infinities.keystone4j.assignment.api.command.domain.CreateDomainCommand;
import com.infinities.keystone4j.assignment.api.command.domain.DeleteDomainCommand;
import com.infinities.keystone4j.assignment.api.command.domain.DisableDomainCommand;
import com.infinities.keystone4j.assignment.api.command.domain.GetDomainByNameCommand;
import com.infinities.keystone4j.assignment.api.command.domain.GetDomainCommand;
import com.infinities.keystone4j.assignment.api.command.domain.ListDomainsCommand;
import com.infinities.keystone4j.assignment.api.command.domain.ListDomainsForGroupsCommand;
import com.infinities.keystone4j.assignment.api.command.domain.ListDomainsForUserCommand;
import com.infinities.keystone4j.assignment.api.command.domain.UpdateDomainCommand;
import com.infinities.keystone4j.assignment.api.command.grant.CreateGrantCommand;
import com.infinities.keystone4j.assignment.api.command.grant.DeleteGrantCommand;
import com.infinities.keystone4j.assignment.api.command.grant.EmitInvalidateUserTokenPersistenceCommand;
import com.infinities.keystone4j.assignment.api.command.grant.GetGrantCommand;
import com.infinities.keystone4j.assignment.api.command.grant.ListGrantsCommand;
import com.infinities.keystone4j.assignment.api.command.group.DeleteGroupCommand;
import com.infinities.keystone4j.assignment.api.command.project.CreateProjectCommand;
import com.infinities.keystone4j.assignment.api.command.project.DeleteProjectCommand;
import com.infinities.keystone4j.assignment.api.command.project.DisableProjectCommand;
import com.infinities.keystone4j.assignment.api.command.project.EmitInvalidateUserProjectTokensNotificationCommand;
import com.infinities.keystone4j.assignment.api.command.project.GetProjectByNameCommand;
import com.infinities.keystone4j.assignment.api.command.project.GetProjectCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectParentsCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectsCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectsForGroupsCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectsForUserCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectsInDomainCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListProjectsInSubtreeCommand;
import com.infinities.keystone4j.assignment.api.command.project.ListUserProjectsCommand;
import com.infinities.keystone4j.assignment.api.command.project.UpdateProjectCommand;
import com.infinities.keystone4j.assignment.api.command.role.CreateRoleCommand;
import com.infinities.keystone4j.assignment.api.command.role.DeleteRoleCommand;
import com.infinities.keystone4j.assignment.api.command.role.GetRoleCommand;
import com.infinities.keystone4j.assignment.api.command.role.GetRolesForUserAndDomainCommand;
import com.infinities.keystone4j.assignment.api.command.role.GetRolesForUserAndProjectCommand;
import com.infinities.keystone4j.assignment.api.command.role.ListRoleAssignmentsCommand;
import com.infinities.keystone4j.assignment.api.command.role.ListRoleAssignmentsForRoleCommand;
import com.infinities.keystone4j.assignment.api.command.role.ListRolesCommand;
import com.infinities.keystone4j.assignment.api.command.role.RemoveRoleFromUserAndProjectCommand;
import com.infinities.keystone4j.assignment.api.command.role.UpdateRoleCommand;
import com.infinities.keystone4j.assignment.api.command.user.AddUserToProjectCommand;
import com.infinities.keystone4j.assignment.api.command.user.DeleteUserCommand;
import com.infinities.keystone4j.assignment.api.command.user.ListUsersForProjectCommand;
import com.infinities.keystone4j.assignment.api.command.user.RemoveUserFromProjectCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.Notifications;

public class AssignmentApiImpl implements AssignmentApi {

	private final static String _PROJECT = "project";
	private final CredentialApi credentialApi;
	// private final IdentityApi identityApi;
	// private final TokenApi tokenApi;
	private final RevokeApi revokeApi;
	private final AssignmentDriver assignmentDriver;
	private IdentityApi identityApi;


	public AssignmentApiImpl(CredentialApi credentialApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver) {
		this.credentialApi = credentialApi;
		// this.identityApi = identityApi;
		this.revokeApi = revokeApi;
		// this.tokenApi = tokenApi;
		this.assignmentDriver = assignmentDriver;
	}

	@Override
	public Domain createDomain(String domainid, Domain domain) throws Exception {
		NonTruncatedCommand<Domain> command = Notifications.created(new CreateDomainCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, domainid, domain), "domain");
		return command.execute();
	}

	@Override
	public List<Domain> listDomains(Hints hints) throws Exception {
		TruncatedCommand<Domain> command = new ResponseTruncatedCommand<Domain>(new ListDomainsCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver), assignmentDriver);
		return command.execute(hints);
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Domain getDomain(String domainid) {
		GetDomainCommand command = new GetDomainCommand(credentialApi, getIdentityApi(), this, revokeApi, assignmentDriver,
				domainid);
		return command.execute();
	}

	@Override
	public Domain updateDomain(String domainid, Domain domain) throws Exception {
		NonTruncatedCommand<Domain> command = Notifications.updated(new UpdateDomainCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, domainid, domain), "domain");
		return command.execute();
	}

	@Override
	public Domain deleteDomain(String domainid) throws Exception {
		NonTruncatedCommand<Domain> command = Notifications.deleted(new DeleteDomainCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, domainid), "domain");
		return command.execute();
	}

	@Override
	public Project createProject(String projectid, Project project) throws Exception {
		NonTruncatedCommand<Project> command = Notifications.created(new CreateProjectCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, projectid, project), _PROJECT);
		return command.execute();
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Project getProject(String projectid) {
		GetProjectCommand command = new GetProjectCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, projectid);
		return command.execute();
	}

	@Override
	public Project updateProject(String tenantid, Project tenant) throws Exception {
		NonTruncatedCommand<Project> command = Notifications.updated(new UpdateProjectCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, tenantid, tenant), _PROJECT);
		return command.execute();
	}

	@Override
	public Project deleteProject(String tenantid) throws Exception {
		NonTruncatedCommand<Project> command = Notifications.deleted(new DeleteProjectCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, tenantid), _PROJECT);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsForUser(String userid, Hints hints) throws Exception {
		ListProjectsForUserCommand command = new ListProjectsForUserCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, userid);
		return command.execute(hints);
	}

	@Override
	public Role createRole(String id, Role role) throws Exception {
		NonTruncatedCommand<Role> command = Notifications.created(new CreateRoleCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, id, role), _PROJECT);
		return command.execute();
	}

	// TODO @manager.response_truncated
	@Override
	public List<Role> listRoles(Hints hints) throws Exception {
		TruncatedCommand<Role> command = new ResponseTruncatedCommand<Role>(new ListRolesCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver), this.assignmentDriver);
		return command.execute(hints);
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Role getRole(String roleid) {
		GetRoleCommand command = new GetRoleCommand(credentialApi, getIdentityApi(), this, revokeApi, assignmentDriver,
				roleid);
		return command.execute();
	}

	@Override
	public Role updateRole(String roleid, Role role) throws Exception {
		NonTruncatedCommand<Role> command = Notifications.updated(new UpdateRoleCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, roleid, role), "role");
		return command.execute();
	}

	@Override
	public Role deleteRole(String roleid) throws Exception {
		NonTruncatedCommand<Role> command = Notifications.deleted(new DeleteRoleCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, roleid), "role");
		return command.execute();
	}

	@Override
	public List<String> getRolesForUserAndProject(String userid, String tenantid) throws Exception {
		GetRolesForUserAndProjectCommand command = new GetRolesForUserAndProjectCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, userid, tenantid);
		return command.execute();
	}

	// TODO ignore @notifications.role_assignment('created')
	@Override
	public void createGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) {
		CreateGrantCommand command = new CreateGrantCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, roleid, userid, groupid, domainid, projectid, inheritedToProjects);
		command.execute();
	}

	@Override
	public Role getGrant(String roleid, String userid, String groupid, String domainid, String projectid, boolean inherited) {
		GetGrantCommand command = new GetGrantCommand(credentialApi, getIdentityApi(), this, revokeApi, assignmentDriver,
				roleid, userid, groupid, domainid, projectid, inherited);
		return command.execute();
	}

	// TODO ignore @notifications.role_assignment('deleted')
	@Override
	public void deleteGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) throws Exception {
		DeleteGrantCommand command = new DeleteGrantCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, roleid, userid, groupid, domainid, projectid, inheritedToProjects);
		command.execute();
	}

	@Override
	public List<Assignment> listRoleAssignmentsForRole(String roleid) {
		ListRoleAssignmentsForRoleCommand command = new ListRoleAssignmentsForRoleCommand(credentialApi, getIdentityApi(),
				this, revokeApi, assignmentDriver, roleid);
		return command.execute();
	}

	@Override
	public List<Project> listProjects(Hints hints) throws Exception {
		TruncatedCommand<Project> command = new ResponseTruncatedCommand<Project>(new ListProjectsCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver), this.assignmentDriver);
		return command.execute(hints);
	}

	@Override
	public List<String> listUserIdsForProject(String projectid) {
		ListUsersForProjectCommand command = new ListUsersForProjectCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, projectid);
		return command.execute();
	}

	@Override
	public List<String> getRolesForUserAndDomain(String userid, String domainid) throws Exception {
		GetRolesForUserAndDomainCommand command = new GetRolesForUserAndDomainCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, userid, domainid);
		return command.execute();
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Domain getDomainByName(String domainName) {
		GetDomainByNameCommand command = new GetDomainByNameCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, domainName);
		return command.execute();
	}

	@Override
	public Project getProjectByName(String projectName, String domainid) {
		GetProjectByNameCommand command = new GetProjectByNameCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, projectName, domainid);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsInDomain(String domainid) throws Exception {
		ListProjectsInDomainCommand command = new ListProjectsInDomainCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, domainid);
		return command.execute();
	}

	@Override
	public List<Domain> listDomainsForUser(String userid, Hints hints) throws Exception {
		ListDomainsForUserCommand command = new ListDomainsForUserCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, userid);
		return command.execute(hints);
	}

	@Override
	public List<Project> listProjectParents(String projectid, String userid) throws Exception {
		ListProjectParentsCommand command = new ListProjectParentsCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, projectid, userid);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsInSubtree(String projectid, String userid) throws Exception {
		ListProjectsInSubtreeCommand command = new ListProjectsInSubtreeCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, projectid, userid);
		return command.execute();
	}

	@Override
	public List<Assignment> listRoleAssignments() {
		ListRoleAssignmentsCommand command = new ListRoleAssignmentsCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver);
		return command.execute();
	}

	@Override
	public void addUserToProject(String projectid, String userid) {
		AddUserToProjectCommand command = new AddUserToProjectCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, projectid, userid);
		command.execute();
	}

	@Override
	public void removeUserFromProject(String projectid, String userid) throws Exception {
		RemoveUserFromProjectCommand command = new RemoveUserFromProjectCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, projectid, userid);
		command.execute();
	}

	@Override
	public List<Project> listUserProjects(String userid, Hints hints) throws Exception {
		TruncatedCommand<Project> command = new ResponseTruncatedCommand<Project>(new ListUserProjectsCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, userid), this.assignmentDriver);
		return command.execute(hints);
	}

	@Override
	public void removeRoleFromUserAndProject(String userid, String tenantid, String roleid) throws Exception {
		RemoveRoleFromUserAndProjectCommand command = new RemoveRoleFromUserAndProjectCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, userid, tenantid, roleid);
		command.execute();
	}

	@Override
	public List<Role> listGrants(String userid, String groupid, String domainid, String projectid, boolean checkIfInherited) {
		ListGrantsCommand command = new ListGrantsCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, userid, groupid, domainid, projectid, checkIfInherited);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsForGroups(List<String> groupids) {
		ListProjectsForGroupsCommand command = new ListProjectsForGroupsCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, groupids);
		return command.execute();
	}

	@Override
	public List<Domain> listDomainsForGroups(List<String> groupids) {
		ListDomainsForGroupsCommand command = new ListDomainsForGroupsCommand(credentialApi, getIdentityApi(), this,
				revokeApi, assignmentDriver, groupids);
		return command.execute();
	}

	@Override
	public void deleteUser(String userid) throws Exception {
		DeleteUserCommand command = new DeleteUserCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, userid);
		command.execute();
	}

	@Override
	public void deleteGroup(String groupid) throws Exception {
		DeleteGroupCommand command = new DeleteGroupCommand(credentialApi, getIdentityApi(), this, revokeApi,
				assignmentDriver, groupid);
		command.execute();
	}

	@Override
	public void assertDomainEnabled(String id, Domain domain) throws Exception {
		if (domain == null) {
			domain = getDomain(id);
		}
		if (!domain.getEnabled()) {
			String msg = String.format("Domain is disabled: %s", id);
			throw new IllegalStateException(msg);
		}
	}

	@Override
	public void assertProjectEnabled(String id, Project project) throws Exception {
		if (project == null) {
			project = getProject(id);
		}
		if (!project.getEnabled()) {
			String msg = String.format("Project is disabled: %s", id);
			throw new IllegalStateException(msg);
		}
	}

	@Override
	public void disableProject(String projectId) throws Exception {
		NonTruncatedCommand<Project> command = Notifications.disabled(new DisableProjectCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, projectId), _PROJECT, false, 1, null);
		command.execute();
	}

	@Override
	public void disableDomain(String domainId) throws Exception {
		NonTruncatedCommand<Domain> command = Notifications.disabled(new DisableDomainCommand(credentialApi,
				getIdentityApi(), this, revokeApi, assignmentDriver, domainId), "domain", false, 1, null);
		command.execute();
	}

	@Override
	public void emitInvalidateUserTokenPersistence(String userid) throws Exception {
		NonTruncatedCommand<User> command = Notifications.internal(new EmitInvalidateUserTokenPersistenceCommand(
				credentialApi, getIdentityApi(), this, revokeApi, assignmentDriver, userid),
				Notifications.INVALIDATE_USER_TOKEN_PERSISTENCE);
		command.execute();
	}

	@Override
	public void emitInvalidateUserProjectTokensNotification(Payload payload) throws Exception {
		NonTruncatedCommand<Project> command = Notifications.internal(
				new EmitInvalidateUserProjectTokensNotificationCommand(credentialApi, getIdentityApi(), this, revokeApi,
						assignmentDriver, payload), Notifications.INVALIDATE_USER_PROJECT_TOKEN_PERSISTENCE);
		command.execute();
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	@Override
	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
		if (this.identityApi != null) {
			this.identityApi.setAssignmentApi(this);
		}
	}

}
