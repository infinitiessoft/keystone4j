package com.infinities.keystone4j.credential;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.credential.Credential;

public interface CredentialApi extends Api {

	Credential createCredential(String id, Credential credential) throws Exception;

	List<Credential> listCredentials(Hints hints) throws Exception;

	Credential getCredential(String credentialid) throws Exception;

	Credential updateCredential(String credentialid, Credential credential) throws Exception;

	Credential deleteCredential(String credentialid) throws Exception;

	void deleteCredentialsForProject(String projectid) throws Exception;

	void deleteCredentialsForUser(String userid) throws Exception;

	List<Credential> listCredentialsForUser(String userId) throws Exception;

}
