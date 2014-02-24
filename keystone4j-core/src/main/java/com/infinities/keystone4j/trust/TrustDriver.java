package com.infinities.keystone4j.trust;

import java.util.List;

import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.trust.model.Trust;

public interface TrustDriver {

	Trust createTrust(Trust trust, List<Role> cleanRoles);

	Trust getTrust(String trustid);

	List<Trust> listTrusts();

	List<Trust> listTrustsForTrustee(String trusteeid);

	List<Trust> listTrustsForTrustor(String trustorid);

	void deleteTrust(String trustid);

}
