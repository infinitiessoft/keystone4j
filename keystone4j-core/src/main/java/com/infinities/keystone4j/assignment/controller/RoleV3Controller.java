/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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

	// void createGrantByUserDomain(String roleid, String userid, String
	// domainid) throws Exception;

	// void createGrantByGroupDomain(String roleid, String groupid, String
	// domainid) throws Exception;

	// CollectionWrapper<Role> listGrantsByUserDomain(String userid, String
	// domainid) throws Exception;

	// CollectionWrapper<Role> listGrantsByGroupDomain(String groupid, String
	// domainid) throws Exception;

	// void checkGrantByUserDomain(String roleid, String userid, String
	// domainid) throws Exception;

	// void checkGrantByGroupDomain(String roleid, String groupid, String
	// domainid) throws Exception;

	// void revokeGrantByUserDomain(String roleid, String userid, String
	// domainid) throws Exception;

	// void revokeGrantByGroupDomain(String roleid, String groupid, String
	// domainid) throws Exception;

	// void createGrantByUserProject(String roleid, String userid, String
	// projectid) throws Exception;

	// void createGrantByGroupProject(String roleid, String groupid, String
	// projectid) throws Exception;

	// CollectionWrapper<Role> listGrantsByUserProject(String userid, String
	// projectid) throws Exception;

	// CollectionWrapper<Role> listGrantsByGroupProject(String groupid, String
	// projectid) throws Exception;

	// void checkGrantByUserProject(String roleid, String userid, String
	// projectid) throws Exception;

	// void checkGrantByGroupProject(String roleid, String groupid, String
	// projectid) throws Exception;

	// void revokeGrantByUserProject(String roleid, String userid, String
	// projectid) throws Exception;

	// void revokeGrantByGroupProject(String roleid, String groupid, String
	// projectid) throws Exception;

	CollectionWrapper<Role> listGrants(String userid, String groupid, String domainid, String projectid) throws Exception;

	void createGrant(String roleid, String userid, String groupid, String domainid, String projectid) throws Exception;

	void checkGrant(String roleid, String userid, String groupid, String domainid, String projectid) throws Exception;

	void revokeGrant(String roleid, String userid, String groupid, String domainid, String projectid) throws Exception;
}
