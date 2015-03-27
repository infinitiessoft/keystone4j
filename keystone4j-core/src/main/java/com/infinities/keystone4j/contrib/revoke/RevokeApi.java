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
