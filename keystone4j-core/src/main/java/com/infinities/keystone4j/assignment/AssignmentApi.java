package com.infinities.keystone4j.assignment;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;

public interface AssignmentApi extends Api {

	Domain createDomain(Domain domain);

	List<Domain> listDomains();

	Domain getDomain(String domainid);

	Domain updateDomain(String domainid, Domain domain);

	Domain deleteDomain(String domainid);

	Project createProject(Project project);

	List<Project> listProjects();

	Project getProject(String projectid);

	Project updateProject(String projectid, Project project);

	Project deleteProject(String projectid);

	List<Project> listProjectsForUser(String userid);

	Role createRole(Role role);

	List<Role> listRoles();

	Role getRole(String roleid);

	Role updateRole(String roleid, Role role);

	Role deleteRole(String roleid);

	List<Role> getRolesForUserAndDomain(String userid, String domainid);

	List<Role> getRolesForUserAndProject(String userid, String projectid);

	void createGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	void createGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	List<Role> listGrantsByUserDomain(String userid, String domainid, boolean inherited);

	List<Role> listGrantsByGroupDomain(String groupid, String domainid, boolean inherited);

	Role getGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	Role getGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	void deleteGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	void deleteGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	List<Assignment> listRoleAssignmentsForRole(String roleid);

	Role getGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited);

	Role getGrantByUserProject(String roleid, String userid, String projectid, boolean inherited);

	void createGrantByGroupProject(String roleid, String groupid, String projectid);

	void createGrantByUserProject(String roleid, String userid, String projectid);

	void deleteGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited);

	void deleteGrantByUserProject(String roleid, String userid, String projectid, boolean inherited);

	List<Role> listGrantsByGroupProject(String groupid, String projectid, boolean inherited);

	List<Role> listGrantsByUserProject(String userid, String projectid, boolean inherited);

	List<User> listUsersForProject(String projectid);

	Domain getDomainByName(String domainName);

	Project getProjectByName(String projectName, String domainid);

	// replace with createGrant
	// addUserToProject(String projectid, String userid);
	// removeUserFromProject(String projectid,String userid);

	// listProjectsInDomain(String domainid);
	// listUserProjects(String userid);
	// getProjectByName(String projectName,String domainid)

	// replace with deleteGrant
	// removeRoleFromUserAndProject(String userid,String projectid,String
	// roleid);
}
