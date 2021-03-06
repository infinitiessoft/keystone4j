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
