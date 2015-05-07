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
package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;

public interface EndpointV3Controller {

	MemberWrapper<Endpoint> createEndpoint(Endpoint endpoint) throws Exception;

	CollectionWrapper<Endpoint> listEndpoints() throws Exception;

	MemberWrapper<Endpoint> getEndpoint(String endpointid) throws Exception;

	MemberWrapper<Endpoint> updateEndpoint(String endpointid, Endpoint endpoint) throws Exception;

	void deleteEndpoint(String endpointid) throws Exception;

}
