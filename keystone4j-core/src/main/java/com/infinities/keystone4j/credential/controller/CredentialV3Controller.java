package com.infinities.keystone4j.credential.controller;

import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.credential.model.CredentialWrapper;
import com.infinities.keystone4j.credential.model.CredentialsWrapper;

public interface CredentialV3Controller {

	CredentialWrapper createCredential(Credential credential);

	CredentialsWrapper listCredentials(int page, int perPage);

	CredentialWrapper getCredential(String credentialid);

	CredentialWrapper updateCredential(String credentialid, Credential credential);

	void deleteCredential(String credentialid);

}
