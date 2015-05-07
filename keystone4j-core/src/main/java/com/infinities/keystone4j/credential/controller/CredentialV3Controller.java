/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
