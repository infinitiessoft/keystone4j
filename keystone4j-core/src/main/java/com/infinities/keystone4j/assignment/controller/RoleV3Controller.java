package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;

public interface RoleV3Controller {

	MemberWrapper<Role> createRole(Role role) throws Exception;

	CollectionWrapper<Role> listRoles() throws Exception;

	MemberWrapper<Role> getRole(String roleid) throws Exception;

	MemberWrapper<Role> updateRole(String roleid, Role role) throws Exception;

	void deleteRole(String roleid) throws Exception;

	// void requireDomainXorProject();

	// void requireUserXorGroup();

	void createGrantByUserDomain(String roleid, String userid, String domainid) throws Exception;

	void createGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception;

	CollectionWrapper<Role> listGrantsByUserDomain(String userid, String domainid) throws Exception;

	CollectionWrapper<Role> listGrantsByGroupDomain(String groupid, String domainid) throws Exception;

	void checkGrantByUserDomain(String roleid, String userid, String domainid) throws Exception;

	void checkGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception;

	void revokeGrantByUserDomain(String roleid, String userid, String domainid) throws Exception;

	void revokeGrantByGroupDomain(String roleid, String groupid, String domainid) throws Exception;

	void createGrantByUserProject(String roleid, String userid, String projectid) throws Exception;

	void createGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception;

	CollectionWrapper<Role> listGrantsByUserProject(String userid, String projectid) throws Exception;

	CollectionWrapper<Role> listGrantsByGroupProject(String groupid, String projectid) throws Exception;

	void checkGrantByUserProject(String roleid, String userid, String projectid) throws Exception;

	void checkGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception;

	void revokeGrantByUserProject(String roleid, String userid, String projectid) throws Exception;

	void revokeGrantByGroupProject(String roleid, String groupid, String projectid) throws Exception;

}
