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
package com.infinities.keystone4j.model.assignment.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;

public class DomainWrapper implements MemberWrapper<Domain> {

	private Domain domain;


	public DomainWrapper() {

	}

	public DomainWrapper(Domain domain) {
		this.domain = domain;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(domain,
		// baseUrl);
	}

	@Override
	public void setRef(Domain ref) {
		this.domain = ref;
	}

	@XmlElement(name = "domain")
	@Override
	public Domain getRef() {
		return domain;
	}
}
