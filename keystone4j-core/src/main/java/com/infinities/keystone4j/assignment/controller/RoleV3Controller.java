package com.infinities.keystone4j.assignment.controller;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.RoleWrapper;
import com.infinities.keystone4j.model.assignment.RolesWrapper;

public interface RoleV3Controller {

	RoleWrapper createRole(Role role);

	RolesWrapper listRoles(String name, int page, int perPage);

	RoleWrapper getRole(String roleid);

	RoleWrapper updateRole(String roleid, Role role);

	void deleteRole(String roleid);

	// void requireDomainXorProject();

	// void requireUserXorGroup();

	void createGrantByUserDomain(String roleid, String userid, String domainid);

	void createGrantByGroupDomain(String roleid, String groupid, String domainid);

	List<Role> listGrantsByUserDomain(String userid, String domainid, int page, int perPage);

	List<Role> listGrantsByGroupDomain(String groupid, String domainid, int page, int perPage);

	void checkGrantByUserDomain(String roleid, String userid, String domainid);

	void checkGrantByGroupDomain(String roleid, String groupid, String domainid);

	void revokeGrantByUserDomain(String roleid, String userid, String domainid);

	void revokeGrantByGroupDomain(String roleid, String groupid, String domainid);

	void createGrantByUserProject(String roleid, String userid, String projectid);

	void createGrantByGroupProject(String roleid, String groupid, String projectid);

	List<Role> listGrantsByUserProject(String userid, String projectid, int page, int perPage);

	List<Role> listGrantsByGroupProject(String groupid, String projectid, int page, int perPage);

	void checkGrantByUserProject(String roleid, String userid, String projectid);

	void checkGrantByGroupProject(String roleid, String groupid, String projectid);

	void revokeGrantByUserProject(String roleid, String userid, String projectid);

	void revokeGrantByGroupProject(String roleid, String groupid, String projectid);

}
