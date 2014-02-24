package com.infinities.keystone4j.trust.controller;

import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustWrapper;
import com.infinities.keystone4j.trust.model.TrustsWrapper;

public interface TrustV3Controller {

	TrustWrapper getTrust(String trustid);

	TrustWrapper createTrust(Trust trust);

	TrustsWrapper listTrusts(String trustorid, String trusteeid, int page, int perPage);

	void deleteTrust(String trustid);

	RolesWrapper listRolesForTrust(String trustid, int page, int perPage);

	void checkRoleForTrust(String trustid, String roleid);

	RoleWrapper getRoleForTrust(String trustid, String roleid);

}
