package com.infinities.keystone4j.trust;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;

public interface TrustApi extends Api {

	Trust createTrust(String id, Trust trust, List<Role> cleanRoles) throws Exception;

	List<Trust> listTrusts();

	List<Trust> listTrustsForTrustor(String trustorid);

	List<Trust> listTrustsForTrustee(String trusteeid);

	// deleted=false
	Trust getTrust(String trustid, boolean deleted);

	void deleteTrust(String trustid) throws Exception;

	void consumeUse(String id) throws Exception;

}
