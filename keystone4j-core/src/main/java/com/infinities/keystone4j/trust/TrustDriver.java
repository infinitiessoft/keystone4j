package com.infinities.keystone4j.trust;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;

public interface TrustDriver {

	Trust createTrust(String trustid, Trust trust, List<Role> cleanRoles);

	Trust getTrust(String trustid, boolean deleted);

	List<Trust> listTrusts();

	List<Trust> listTrustsForTrustee(String trusteeid);

	List<Trust> listTrustsForTrustor(String trustorid);

	void deleteTrust(String trustid);

	void consumeUse(String trustid) throws Exception;

}
