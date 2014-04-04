package com.infinities.keystone4j.trust.controller;

import com.infinities.keystone4j.model.assignment.RoleWrapper;
import com.infinities.keystone4j.model.assignment.RolesWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustWrapper;
import com.infinities.keystone4j.model.trust.TrustsWrapper;

public interface TrustV3Controller {

	TrustWrapper getTrust(String trustid);

	TrustWrapper createTrust(Trust trust);

	TrustsWrapper listTrusts(String trustorid, String trusteeid, int page, int perPage);

	void deleteTrust(String trustid);

	RolesWrapper listRolesForTrust(String trustid, int page, int perPage);

	void checkRoleForTrust(String trustid, String roleid);

	RoleWrapper getRoleForTrust(String trustid, String roleid);

}
