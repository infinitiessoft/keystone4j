package com.infinities.keystone4j.assignment;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.UserDomainGrant;
import com.infinities.keystone4j.model.assignment.UserProjectGrant;
import com.infinities.keystone4j.model.identity.User;

public interface AssignmentDriver {

	Project getProject(String projectid);

	Project getProjectByName(String projectName, String domainid);

	List<User> listUsersForProject(String projectid);

	// void createGrant();

	// List<Role> listGrants();

	// Role getGrant();

	// void deleteGrant();

	List<Project> listProjects(String domainid);

	List<Project> listProjectsForUser(String userid, List<String> groupids);

	void addRoleToUserAndProject(String userid, String projectid, String roleid);

	void removeRoleFromUserAndProject(String userid, String projectid, String roleid);

	List<Assignment> listRoleAssignments();

	Project createProject(Project project);

	Project updateProject(String projectid, Project project);

	void deleteProject(String projectid);

	Domain createDomain(Domain domain);

	List<Domain> listDomains();

	Domain getDomain(String domainid);

	Domain getDomainByName(String domainName);

	Domain updateDomain(String domainid, Domain domain);

	void deleteDomain(String domainid);

	Role createRole(Role role);

	List<Role> listRoles();

	Role getRole(String roleid);

	Role updateRole(String roleid, Role role);

	void deleteRole(String roleid);

	// void deleteUser();
	//
	// void deleteGroup();

	// _get_metadata
	GroupProjectGrant getGroupProjectGrant(String groupid, String projectid, String roleid);

	GroupDomainGrant getGroupDomainGrant(String groupid, String domainid, String roleid);

	List<GroupDomainGrant> getGroupDomainGrants(String groupid, String domainid);

	List<UserDomainGrant> getUserDomainGrants(String userid, String domainid);

	List<GroupProjectGrant> getGroupProjectGrants(String groupid, String projectid);

	List<UserProjectGrant> getUserProjectGrants(String userid, String projectid);

	UserProjectGrant getUserProjectGrant(String userid, String projectid, String roleid);

	UserDomainGrant getUserDomainGrant(String userid, String domainid, String roleid);

	void deleteGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	void deleteGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited);

	void deleteGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	void deleteGrantByUserProject(String roleid, String userid, String projectid, boolean inherited);

	Role getGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	Role getGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited);

	Role getGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	Role getGrantByUserProject(String roleid, String userid, String projectid, boolean inherited);

	void createGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited);

	void createGrantByGroupProject(String roleid, String groupid, String projectid);

	void createGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited);

	void createGrantByUserProject(String roleid, String userid, String projectid);

	List<Role> listGrantsByGroupDomain(String groupid, String domainid, boolean inherited);

	List<Role> listGrantsByGroupProject(String groupid, String projectid, boolean inherited);

	List<Role> listGrantsByUserProject(String userid, String projectid, boolean inherited);

	List<Role> listGrantsByUserDomain(String userid, String domainid, boolean inherited);

}
