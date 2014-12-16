package com.infinities.keystone4j.trust.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;

public interface TrustV3Controller {

	MemberWrapper<Trust> getTrust(String trustid) throws Exception;

	MemberWrapper<Trust> createTrust(Trust trust) throws Exception;

	CollectionWrapper<Trust> listTrusts() throws Exception;

	void deleteTrust(String trustid) throws Exception;

	CollectionWrapper<Role> listRolesForTrust(String trustid) throws Exception;

	void checkRoleForTrust(String trustid, String roleid) throws Exception;

	MemberWrapper<Role> getRoleForTrust(String trustid, String roleid) throws Exception;

}
