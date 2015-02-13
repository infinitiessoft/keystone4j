package com.infinities.keystone4j.credential.driver;

import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.TruncatedFunction;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.driver.function.ListCredentialsFunction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.CredentialDao;
import com.infinities.keystone4j.model.credential.Credential;

public class CredentialJpaDriver implements CredentialDriver {

	private final CredentialDao credentialDao;


	// private final BlobDao blobDao;

	public CredentialJpaDriver() {
		credentialDao = new CredentialDao();
		// blobDao = new BlobDao();
	}

	@Override
	public Credential createCredential(String credentialid, Credential credential) {
		credentialDao.persist(credential);

		return credential;
	}

	@Override
	public List<Credential> listCredentials(Hints hints) throws Exception {
		ListFunction<Credential> function = new TruncatedFunction<Credential>(new ListCredentialsFunction());
		return function.execute(hints);
	}

	@Override
	public List<Credential> listCredentialsForUser(String userid) {
		return credentialDao.listCredentialsForUser(userid);
	}

	@Override
	public Credential getCredential(String credentialid) {
		Credential credential = credentialDao.findById(credentialid);
		if (credential == null) {
			throw Exceptions.CredentialNotFoundException.getInstance(null, credentialid);
		}
		return credential;
	}

	@Override
	public Credential updateCredential(String credentialid, Credential credential) {
		Credential ref = getCredential(credentialid);
		if (credential.isBlobUpdated()) {
			ref.setBlob(credential.getBlob());
		}
		if (credential.isDescriptionUpdated()) {
			ref.setDescription(credential.getDescription());
		}
		if (credential.isExtraUpdated()) {
			ref.setExtra(credential.getExtra());
		}
		if (credential.isProjectUpdated()) {
			ref.setProjectId(credential.getProjectId());
		}
		if (credential.isTypeUpdated()) {
			ref.setType(credential.getType());
		}
		if (credential.isUserUpdated()) {
			ref.setUserId(credential.getUserId());
		}

		return credentialDao.merge(ref);
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

	@Override
	public Integer getListLimit() {
		return null;
	}

}
