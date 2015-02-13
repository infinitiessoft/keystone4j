package com.infinities.keystone4j.credential.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;

public interface CredentialV3Controller {

	MemberWrapper<Credential> createCredential(Credential credential) throws Exception;

	CollectionWrapper<Credential> listCredentials() throws Exception;

	MemberWrapper<Credential> getCredential(String credentialid) throws Exception;

	MemberWrapper<Credential> updateCredential(String credentialid, Credential credential) throws Exception;

	void deleteCredential(String credentialid) throws Exception;

}
