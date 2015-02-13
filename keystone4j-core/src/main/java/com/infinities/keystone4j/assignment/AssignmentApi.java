package com.infinities.keystone4j.assignment;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand.Payload;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;

public interface AssignmentApi extends Api {

	//
	// keystone.assignment.core.Manager.create_domain 20141226
	Domain createDomain(String id, Domain domain) throws Exception;

	//
	// keystone.assignment.core.Manager.list_domains 20141226
	List<Domain> listDomains(Hints hints) throws Exception;

	//
	// keystone.assignment.core.Manager.get_domain 20141226
	Domain getDomain(String domainid) throws Exception;

	//
	// keystone.assignment.core.Manager.update_domain 20141226
	Domain updateDomain(String domainid, Domain domain) throws Exception;

	//
	// keystone.assignment.core.Manager.delete_domain 20141226
	Domain deleteDomain(String domainid) throws Exception;

	//
	// keystone.assignment.core.Manager.create_project 20141224
	Project createProject(String id, Project project) throws Exception;

	//
	// keystone.assignment.core.Manager.list_projects 20141226
	List<Project> listProjects(Hints hints) throws Exception;

	// keystone.assignment.core.Manager.get_project 20141226
	Project getProject(String projectid) throws Exception;

	//
	// keystone.assignment.core.Manager.update_project 20141224
	Project updateProject(String projectid, Project project) throws Exception;

	//
	// keystone.assignment.core.Manager.delete_project 20141224
	Project deleteProject(String projectid) throws Exception;

	//
	// keystone.assignment.core.Manager.list_projects_for_user 20141226
	List<Project> listProjectsForUser(String userid, Hints hints) throws Exception;

	// keystone.assignment.core.Manager.create_role 20141226
	Role createRole(String id, Role role) throws Exception;

	// keystone.assignment.core.Manager.list_roles 20141226
	List<Role> listRoles(Hints hints) throws Exception;

	// keystone.assignment.core.Manager.get_role 20141226
	Role getRole(String roleid) throws Exception;

	// keystone.assignment.core.Manager.update_role 20141226
	Role updateRole(String roleid, Role role) throws Exception;

	// keystone.assignment.core.Manager.delete_role 20141226
	Role deleteRole(String roleid) throws Exception;

	// keystone.assignment.core.Manager.get_roles_for_user_and_domain 20141226
	List<String> getRolesForUserAndDomain(String userid, String domainid) throws Exception;

	// keystone.assignment.core.Manager.get_roles_for_user_and_project 20141224
	List<String> getRolesForUserAndProject(String userid, String projectid) throws Exception;

	List<Assignment> listRoleAssignmentsForRole(String roleid) throws Exception;

	List<Assignment> listRoleAssignments() throws Exception;

	// keystone.assignment.core.Driver.list_user_ids_for_project 20141226
	List<String> listUserIdsForProject(String projectid) throws Exception;

	// keystone.assignment.core.Manager.get_domain_by_name 20141226
	Domain getDomainByName(String domainName) throws Exception;

	// keystone.assignment.core.Driver.get_project_by_name 20141226
	Project getProjectByName(String projectName, String domainid) throws Exception;

	// keystone.assignment.core.Manager.list_projects_in_domain 20141226
	List<Project> listProjectsInDomain(String domainid) throws Exception;

	// keystone.assignment.core.Manager.assert_domain_enabled 20141226
	// void assertDomainEnabled(String id, Domain domain)throws Exception;

	// keystone.assignment.core.Manager.assert_project_enabled 20141226
	// void assertProjectEnabled(String id, Project project)throws Exception;

	List<Project> listProjectsForGroups(List<String> groupids) throws Exception;

	// keystone.assignment.core.Manager.list_domains_for_user 20141226
	List<Domain> listDomainsForUser(String userid, Hints hints) throws Exception;

	List<Domain> listDomainsForGroups(List<String> groupids) throws Exception;

	// keystone.assignment.core.Manager.add_user_to_project 20141226
	void addUserToProject(String projectid, String userid) throws Exception;

	// keystone.assignment.core.Manager.remove_user_from_project 20141226
	void removeUserFromProject(String projectid, String userid) throws Exception;

	// listProjectsInDomain(String domainid)throws Exception;

	// keystone.assignment.core.Manager.list_user_project 20141226
	List<Project> listUserProjects(String userid, Hints hints) throws Exception;

	// keystone.assignment.core.Manager.get_project_by_name 20141226
	// getProjectByName(String projectName,String domainid)

	// keystone.assignment.core.Manager.remove_role_from_user_and_project
	// 20141226
	// replace with deleteGrant
	void removeRoleFromUserAndProject(String userid, String tenantid, String roleid) throws Exception;

	// userid=null
	// keystone.assignment.core.Manager.list_project_parents 20141226
	List<Project> listProjectParents(String projectid, String userid) throws Exception;

	// userid=null
	// keystone.assignment.core.Manager.list_projects_in_subtree 20141226
	List<Project> listProjectsInSubtree(String projectid, String userid) throws Exception;

	// keystone.assignment.core.Manager.create_grant 20141226
	void createGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) throws Exception;

	void deleteGrant(String roleid, String userid, String groupid, String domainid, String projectid, boolean inherited)
			throws Exception;

	List<Role> listGrants(String userid, String groupid, String domainid, String projectid, boolean checkIfInherited)
			throws Exception;

	Role getGrant(String roleid, String userid, String groupid, String domainid, String projectid, boolean checkIfInherited)
			throws Exception;

	void deleteUser(String userid) throws Exception;

	void deleteGroup(String groupid) throws Exception;

	void assertDomainEnabled(String id, Domain domain) throws Exception;

	void assertProjectEnabled(String id, Project project) throws Exception;

	void disableProject(String projectId) throws Exception;

	void disableDomain(String domainId) throws Exception;

	void emitInvalidateUserTokenPersistence(String userid) throws Exception;

	void emitInvalidateUserProjectTokensNotification(Payload payload) throws Exception;

	void setIdentityApi(IdentityApi identityApi);

}
