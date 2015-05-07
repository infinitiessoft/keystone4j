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
package com.infinities.keystone4j.contrib.revoke;

import java.util.Calendar;

import com.infinities.keystone4j.contrib.revoke.model.Model.TokenValues;
import com.infinities.keystone4j.contrib.revoke.model.RevokeEvent;

public interface RevokeApi {

	void revokeByGrant(String roleid, String userid, String domainid, String projectid);

	// projectId=null,domainId=null
	void revokeByExpiration(String userId, Calendar expiresAt, String projectId, String domainId);

	void revokeByAuditChainId(String auditChainId, String projectId, String domainId);

	void revokeByAuditId(String auditId);

	void revokeByUser(String userid);

	void revokeByUserAndProject(String userid, String projectId);

	void revokeByProjectRoleAssignment(String projectId, String roleId);

	void revokeByDomainRoleAssignment(String domainId, String roleId);

	void checkToken(TokenValues tokenValues) throws Exception;

	void revoke(RevokeEvent event);

}
