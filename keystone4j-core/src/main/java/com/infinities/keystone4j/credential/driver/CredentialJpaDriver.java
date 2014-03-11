package com.infinities.keystone4j.credential.driver;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.CredentialDao;

public class CredentialJpaDriver implements CredentialDriver {

	private final CredentialDao credentialDao;


	public CredentialJpaDriver() {
		credentialDao = new CredentialDao();
	}

	@Override
	public Credential createCredential(Credential credential) {
		credentialDao.persist(credential);
		return credential;
	}

	@Override
	public List<Credential> listCredentials(String userid) {
		return credentialDao.listCredentialsForUser(userid);
	}

	@Override
	public Credential getCredential(String credentialid) {
		Credential credential = credentialDao.findById(credentialid);
		if (credential != null) {
			throw Exceptions.CredentialNotFoundException.getInstance(null, credentialid);
		}
		return credential;
	}

	@Override
	public Credential updateCredential(String credentialid, Credential credential) {
		Credential oldCredential = getCredential(credentialid);
		if (credential.isBlobUpdated()) {
			oldCredential.setBlob(credential.getBlob());
		}
		if (credential.isDescriptionUpdated()) {
			oldCredential.setDescription(credential.getDescription());
		}
		if (credential.isExtraUpdated()) {
			oldCredential.setExtra(credential.getExtra());
		}
		if (credential.isProjectUpdated()) {
			oldCredential.setProject(credential.getProject());
		}
		if (credential.isTypeUpdated()) {
			oldCredential.setType(credential.getType());
		}
		if (credential.isUserUpdated()) {
			oldCredential.setUser(credential.getUser());
		}

		return credentialDao.merge(oldCredential);
	}

	@Override
	public void deleteCredential(String credentialid) {
		Credential credential = getCredential(credentialid);
		credentialDao.remove(credential);
	}

	@Override
	public void deleteCredentialsForProject(String projectid) {
		credentialDao.removeCredentialForProject(projectid);
	}

	@Override
	public void deleteCredentialsForUser(String userid) {
		credentialDao.removeCredentialForUser(userid);
	}

}
