package com.infinities.keystone4j.assignment.api;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.domain.CreateDomainCommand;
import com.infinities.keystone4j.assignment.command.domain.DeleteDomainCommand;
import com.infinities.keystone4j.assignment.command.domain.GetDomainByNameCommand;
import com.infinities.keystone4j.assignment.command.domain.GetDomainCommand;
import com.infinities.keystone4j.assignment.command.domain.ListDomainsCommand;
import com.infinities.keystone4j.assignment.command.domain.UpdateDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.CreateGrantByGroupDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.CreateGrantByGroupProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.CreateGrantByUserDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.CreateGrantByUserProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.DeleteGrantByGroupDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.DeleteGrantByGroupProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.DeleteGrantByUserDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.DeleteGrantByUserProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.GetGrantByGroupDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.GetGrantByGroupProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.GetGrantByUserDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.GetGrantByUserProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.ListGrantsByGroupDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.ListGrantsByGroupProjectCommand;
import com.infinities.keystone4j.assignment.command.grant.ListGrantsByUserDomainCommand;
import com.infinities.keystone4j.assignment.command.grant.ListGrantsByUserProjectCommand;
import com.infinities.keystone4j.assignment.command.project.CreateProjectCommand;
import com.infinities.keystone4j.assignment.command.project.DeleteProjectCommand;
import com.infinities.keystone4j.assignment.command.project.GetProjectByNameCommand;
import com.infinities.keystone4j.assignment.command.project.GetProjectCommand;
import com.infinities.keystone4j.assignment.command.project.ListProjectsCommand;
import com.infinities.keystone4j.assignment.command.project.ListProjectsForUserCommand;
import com.infinities.keystone4j.assignment.command.project.UpdateProjectCommand;
import com.infinities.keystone4j.assignment.command.role.CreateRoleCommand;
import com.infinities.keystone4j.assignment.command.role.DeleteRoleCommand;
import com.infinities.keystone4j.assignment.command.role.GetRoleCommand;
import com.infinities.keystone4j.assignment.command.role.GetRolesForUserAndDomainCommand;
import com.infinities.keystone4j.assignment.command.role.GetRolesForUserAndProjectCommand;
import com.infinities.keystone4j.assignment.command.role.ListRoleAssignmentsForRoleCommand;
import com.infinities.keystone4j.assignment.command.role.ListRolesCommand;
import com.infinities.keystone4j.assignment.command.role.UpdateRoleCommand;
import com.infinities.keystone4j.assignment.command.user.ListUsersForProjectCommand;
import com.infinities.keystone4j.assignment.model.Assignment;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class AssignmentApiImpl implements AssignmentApi {

	private final CredentialApi credentialApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final AssignmentDriver assignmentDriver;


	public AssignmentApiImpl(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentDriver assignmentDriver) {
		this.credentialApi = credentialApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.assignmentDriver = assignmentDriver;
	}

	@Override
	public Domain createDomain(Domain domain) {
		CreateDomainCommand command = new CreateDomainCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				domain);
		return command.execute();
	}

	@Override
	public List<Domain> listDomains() {
		ListDomainsCommand command = new ListDomainsCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver);
		return command.execute();
	}

	@Override
	public Domain getDomain(String domainid) {
		GetDomainCommand command = new GetDomainCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				domainid);
		return command.execute();
	}

	@Override
	public Domain updateDomain(String domainid, Domain domain) {
		UpdateDomainCommand command = new UpdateDomainCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				domainid, domain);
		return command.execute();
	}

	@Override
	public Domain deleteDomain(String domainid) {
		DeleteDomainCommand command = new DeleteDomainCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				domainid);
		return command.execute();
	}

	@Override
	public Project createProject(Project project) {
		CreateProjectCommand command = new CreateProjectCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, project);
		return command.execute();
	}

	@Override
	public Project getProject(String projectid) {
		GetProjectCommand command = new GetProjectCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				projectid);
		return command.execute();
	}

	@Override
	public Project updateProject(String projectid, Project project) {
		UpdateProjectCommand command = new UpdateProjectCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, projectid, project);
		return command.execute();
	}

	@Override
	public Project deleteProject(String projectid) {
		DeleteProjectCommand command = new DeleteProjectCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, projectid);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsForUser(String userid) {
		ListProjectsForUserCommand command = new ListProjectsForUserCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, userid);
		return command.execute();
	}

	@Override
	public Role createRole(Role role) {
		CreateRoleCommand command = new CreateRoleCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver, role);
		return command.execute();
	}

	@Override
	public List<Role> listRoles() {
		ListRolesCommand command = new ListRolesCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver);
		return command.execute();
	}

	@Override
	public Role getRole(String roleid) {
		GetRoleCommand command = new GetRoleCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver, roleid);
		return command.execute();
	}

	@Override
	public Role updateRole(String roleid, Role role) {
		UpdateRoleCommand command = new UpdateRoleCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				roleid, role);
		return command.execute();
	}

	@Override
	public Role deleteRole(String roleid) {
		DeleteRoleCommand command = new DeleteRoleCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver,
				roleid);
		return command.execute();
	}

	@Override
	public List<Role> getRolesForUserAndProject(String userid, String projectid) {
		GetRolesForUserAndProjectCommand command = new GetRolesForUserAndProjectCommand(credentialApi, identityApi,
				tokenApi, this, assignmentDriver, userid, projectid);
		return command.execute();
	}

	@Override
	public void createGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		CreateGrantByUserDomainCommand command = new CreateGrantByUserDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, userid, domainid, inherited);
		command.execute();
	}

	@Override
	public void createGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		CreateGrantByGroupDomainCommand command = new CreateGrantByGroupDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, groupid, domainid, inherited);
		command.execute();
	}

	@Override
	public void createGrantByGroupProject(String roleid, String groupid, String projectid) {
		CreateGrantByGroupProjectCommand command = new CreateGrantByGroupProjectCommand(credentialApi, identityApi,
				tokenApi, this, assignmentDriver, roleid, groupid, projectid);
		command.execute();
	}

	@Override
	public void createGrantByUserProject(String roleid, String userid, String projectid) {
		CreateGrantByUserProjectCommand command = new CreateGrantByUserProjectCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, userid, projectid);
		command.execute();
	}

	@Override
	public List<Role> listGrantsByUserDomain(String userid, String domainid, boolean inherited) {
		ListGrantsByUserDomainCommand command = new ListGrantsByUserDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, userid, domainid, inherited);
		return command.execute();
	}

	@Override
	public List<Role> listGrantsByGroupDomain(String groupid, String domainid, boolean inherited) {
		ListGrantsByGroupDomainCommand command = new ListGrantsByGroupDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, groupid, domainid, inherited);
		return command.execute();
	}

	@Override
	public List<Role> listGrantsByGroupProject(String groupid, String projectid, boolean inherited) {
		ListGrantsByGroupProjectCommand command = new ListGrantsByGroupProjectCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, groupid, projectid, inherited);
		return command.execute();
	}

	@Override
	public List<Role> listGrantsByUserProject(String userid, String projectid, boolean inherited) {
		ListGrantsByUserProjectCommand command = new ListGrantsByUserProjectCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, userid, projectid, inherited);
		return command.execute();
	}

	@Override
	public Role getGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		GetGrantByUserDomainCommand command = new GetGrantByUserDomainCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, roleid, userid, domainid, inherited);
		return command.execute();
	}

	@Override
	public Role getGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		GetGrantByGroupDomainCommand command = new GetGrantByGroupDomainCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, roleid, groupid, domainid, inherited);
		return command.execute();
	}

	@Override
	public void getGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited) {
		GetGrantByGroupProjectCommand command = new GetGrantByGroupProjectCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, groupid, projectid, inherited);
		command.execute();
	}

	@Override
	public void getGrantByUserProject(String roleid, String userid, String projectid, boolean inherited) {
		GetGrantByUserProjectCommand command = new GetGrantByUserProjectCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, roleid, userid, projectid, inherited);
		command.execute();
	}

	@Override
	public void deleteGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		DeleteGrantByUserDomainCommand command = new DeleteGrantByUserDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, userid, domainid, inherited);
		command.execute();
	}

	@Override
	public void deleteGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		DeleteGrantByGroupDomainCommand command = new DeleteGrantByGroupDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, groupid, domainid, inherited);
		command.execute();
	}

	@Override
	public void deleteGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited) {
		DeleteGrantByGroupProjectCommand command = new DeleteGrantByGroupProjectCommand(credentialApi, identityApi,
				tokenApi, this, assignmentDriver, roleid, groupid, projectid, inherited);
		command.execute();
	}

	@Override
	public void deleteGrantByUserProject(String roleid, String userid, String projectid, boolean inherited) {
		DeleteGrantByUserProjectCommand command = new DeleteGrantByUserProjectCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, roleid, userid, projectid, inherited);
		command.execute();
	}

	@Override
	public List<Assignment> listRoleAssignmentsForRole(String roleid) {
		ListRoleAssignmentsForRoleCommand command = new ListRoleAssignmentsForRoleCommand(credentialApi, identityApi,
				tokenApi, this, assignmentDriver, roleid);
		return command.execute();
	}

	@Override
	public List<Project> listProjects() {
		ListProjectsCommand command = new ListProjectsCommand(credentialApi, identityApi, tokenApi, this, assignmentDriver);
		return command.execute();
	}

	@Override
	public List<User> listUsersForProject(String projectid) {
		ListUsersForProjectCommand command = new ListUsersForProjectCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, projectid);
		return command.execute();
	}

	@Override
	public List<Role> getRolesForUserAndDomain(String userid, String domainid) {
		GetRolesForUserAndDomainCommand command = new GetRolesForUserAndDomainCommand(credentialApi, identityApi, tokenApi,
				this, assignmentDriver, userid, domainid);
		return command.execute();
	}

	@Override
	public Domain getDomainByName(String domainName) {
		GetDomainByNameCommand command = new GetDomainByNameCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, domainName);
		return command.execute();
	}

	@Override
	public Project getProjectByName(String projectName, String domainid) {
		GetProjectByNameCommand command = new GetProjectByNameCommand(credentialApi, identityApi, tokenApi, this,
				assignmentDriver, projectName, domainid);
		return command.execute();
	}

}
