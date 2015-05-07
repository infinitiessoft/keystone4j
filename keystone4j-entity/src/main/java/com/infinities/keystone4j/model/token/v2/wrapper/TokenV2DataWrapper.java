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
package com.infinities.keystone4j.model.token.v2.wrapper;

import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;

public class TokenV2DataWrapper implements MemberWrapper<Access>, ITokenDataWrapper {

	private Access access;


	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	@Override
	public void setRef(Access ref) {
		this.access = ref;
	}

	@XmlTransient
	@Override
	public Access getRef() {
		return access;
	}

}
