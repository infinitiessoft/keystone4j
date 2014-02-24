package com.infinities.keystone4j.credential;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.credential.model.Credential;

public interface CredentialApi extends Api {

	Credential createCredential(Credential credential);

	List<Credential> listCredentials();

	Credential getCredential(String credentialid);

	Credential updateCredential(String credentialid, Credential credential);

	Credential deleteCredential(String credentialid);

	void deleteCredentialsForProject(String projectid);

	void deleteCredentialsForUser(String userid);

}
