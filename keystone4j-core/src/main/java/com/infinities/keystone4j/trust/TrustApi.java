package com.infinities.keystone4j.trust;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.trust.model.Trust;

public interface TrustApi extends Api {

	Trust createTrust(Trust trust, List<Role> cleanRoles);

	List<Trust> listTrusts();

	List<Trust> listTrustsForTrustor(String trustorid);

	List<Trust> listTrustsForTrustee(String trusteeid);

	Trust getTrust(String trustid);

	void deleteTrust(String trustid);

}
