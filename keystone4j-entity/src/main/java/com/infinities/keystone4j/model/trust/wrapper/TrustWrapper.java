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
package com.infinities.keystone4j.model.trust.wrapper;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.trust.Trust;

public class TrustWrapper implements MemberWrapper<Trust> {

	private Trust trust;


	public TrustWrapper() {

	}

	public TrustWrapper(Trust trust) {
		this.trust = trust;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(trust,
		// baseUrl);
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	@Override
	public void setRef(Trust ref) {
		this.trust = ref;
	}

	@Override
	public Trust getRef() {
		return trust;
	}
}
