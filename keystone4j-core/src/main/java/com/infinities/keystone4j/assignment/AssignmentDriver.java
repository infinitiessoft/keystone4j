package com.infinities.keystone4j.assignment;

import java.util.List;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Metadata;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;

public interface AssignmentDriver extends Driver {

	Project getProject(String projectid);

	Project getProjectByName(String projectName, String domainid);

	// List<User> listUsersForProject(String projectid);

	List<Project> listProjects(Hints hints) throws Exception;

	List<Project> listProjectsForUser(String userid, List<String> groupids, Hints hints);

	void addRoleToUserAndProject(String userid, String projectid, String roleid);

	void removeRoleFromUserAndProject(String userid, String projectid, String roleid);

	List<Assignment> listRoleAssignments();

	Project createProject(String tenantid, Project tenant);

	Project updateProject(String projectid, Project project);

	Project deleteProject(String projectid);

	Domain createDomain(String domainid, Domain domain);

	List<Domain> listDomains(Hints hints) throws Exception;

	Domain getDomain(String domainid);

	Domain getDomainByName(String domainName);

	Domain updateDomain(String domainid, Domain domain);

	void deleteDomain(String domainid);

	Role createRole(String id, Role role);

	List<Role> listRoles(Hints hints) throws Exception;

	Role getRole(String roleid);

	Role updateRole(String roleid, Role role);

	void deleteRole(String roleid);

	// void deleteUser();
	//
	// void deleteGroup();

	// _get_metadata
	// GroupProjectGrant getGroupProjectGrant(String groupid, String projectid,
	// String roleid);

	// GroupDomainGrant getGroupDomainGrant(String groupid, String domainid,
	// String roleid);

	// List<GroupDomainGrant> getGroupDomainGrants(String groupid, String
	// domainid);

	// List<UserDomainGrant> getUserDomainGrants(String userid, String
	// domainid);

	// List<GroupProjectGrant> getGroupProjectGrants(String groupid, String
	// projectid);

	// List<UserProjectGrant> getUserProjectGrants(String userid, String
	// projectid);

	// UserProjectGrant getUserProjectGrant(String userid, String projectid,
	// String roleid);

	// UserDomainGrant getUserDomainGrant(String userid, String domainid, String
	// roleid);

	List<Project> listProjectsInSubtree(String projectid) throws Exception;

	boolean isLeafProject(String tenantid);

	List<String> getGroupProjectRoles(List<String> groupids, String projectid, String domainid) throws Exception;

	// inheritedToProjects = false
	void createGrant(String roleid, String userid, String groupid, String projectid, String domainid,
			boolean inheritedToProjects);

	void deleteGrant(String roleid, String userid, String groupid, String domainid, String projectid, boolean inherited);

	List<Project> listProjectParents(String projectid) throws Exception;

	List<Domain> listDomainsForUser(String userid, List<String> groupids, Hints hints);

	List<Project> listProjectsInDomain(String domainid) throws Exception;

	List<Project> listUserProjects(String userid, Hints hints);

	List<String> listUserIdsForProject(String projectid);

	// inheritedToProjects=false
	Role getGrant(String roleid, String userid, String groupid, String domainid, String projectid, boolean inherited);

	// inheritedToProjects= false
	List<Role> listGrants(String userid, String groupid, String domainid, String projectid, boolean inherited);

	List<Project> listProjectsForGroups(List<String> groupids);

	List<Domain> listDomainsForGroups(List<String> groupids);

	void deleteUser(String userid);

	void deleteGroup(String groupid);

	Metadata getMetadata(String userid, String tenantid, String domainid, String groupid);

	List<String> rolesFromRoleDicts(List<com.infinities.keystone4j.model.assignment.Metadata.Role> roles, boolean b);

}
