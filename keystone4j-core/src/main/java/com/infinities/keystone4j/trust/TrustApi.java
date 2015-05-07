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
package com.infinities.keystone4j.trust;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;

public interface TrustApi extends Api {

	Trust createTrust(String id, Trust trust, List<Role> cleanRoles) throws Exception;

	List<Trust> listTrusts();

	List<Trust> listTrustsForTrustor(String trustorid);

	List<Trust> listTrustsForTrustee(String trusteeid);

	// deleted=false
	Trust getTrust(String trustid, boolean deleted);

	void deleteTrust(String trustid) throws Exception;

	void consumeUse(String id) throws Exception;

}
