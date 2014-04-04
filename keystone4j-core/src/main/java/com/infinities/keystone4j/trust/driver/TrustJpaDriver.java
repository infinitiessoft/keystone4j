package com.infinities.keystone4j.trust.driver;

import java.util.List;

import com.infinities.keystone4j.jpa.impl.TrustDao;
import com.infinities.keystone4j.jpa.impl.TrustRoleDao;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.trust.TrustDriver;

public class TrustJpaDriver implements TrustDriver {

	private final TrustDao trustDao;
	private final TrustRoleDao trustRoleDao;


	public TrustJpaDriver() {
		trustDao = new TrustDao();
		trustRoleDao = new TrustRoleDao();
	}

	@Override
	public Trust createTrust(Trust trust, List<Role> roles) {
		trustDao.persist(trust);
		for (Role role : roles) {
			TrustRole trustRole = new TrustRole(trust, role);
			trustRoleDao.persist(trustRole);
			trust.getTrustRoles().add(trustRole);
		}
		trustDao.merge(trust);
		return trust;
	}

	@Override
	public Trust getTrust(String trustid) {
		return trustDao.findById(trustid);
	}

	@Override
	public List<Trust> listTrusts() {
		return trustDao.findAll();
	}

	@Override
	public List<Trust> listTrustsForTrustee(String trusteeid) {
		return trustDao.listTrustsForTrustee(trusteeid);
	}

	@Override
	public List<Trust> listTrustsForTrustor(String trustorid) {
		return trustDao.listTrustsForTrustor(trustorid);
	}

	@Override
	public void deleteTrust(String trustid) {
		trustDao.remove(trustid);
	}

}
