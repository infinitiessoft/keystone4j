package com.infinities.keystone4j.credential;

import java.util.List;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.credential.Credential;

public interface CredentialDriver extends Driver {

	Credential createCredential(String credentialid, Credential credential) throws Exception;

	List<Credential> listCredentials(Hints hints) throws Exception;

	Credential getCredential(String credentialid) throws Exception;

	Credential updateCredential(String credentialid, Credential credential) throws Exception;

	void deleteCredential(String credentialid) throws Exception;

	void deleteCredentialsForProject(String projectid) throws Exception;

	void deleteCredentialsForUser(String userid) throws Exception;

	List<Credential> listCredentialsForUser(String userid) throws Exception;

}
