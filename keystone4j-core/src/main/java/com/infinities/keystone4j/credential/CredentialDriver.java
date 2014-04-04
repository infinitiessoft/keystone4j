package com.infinities.keystone4j.credential;

import java.util.List;

import com.infinities.keystone4j.model.credential.Credential;

public interface CredentialDriver {

	Credential createCredential(Credential credential);

	List<Credential> listCredentials(String userid);

	Credential getCredential(String credentialid);

	Credential updateCredential(String credentialid, Credential credential);

	void deleteCredential(String credentialid);

	void deleteCredentialsForProject(String projectid);

	void deleteCredentialsForUser(String userid);

}
