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
package com.infinities.keystone4j.model.auth;

import java.io.Serializable;
import java.util.List;

import com.infinities.keystone4j.model.token.Token;

public class RevokedWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1186349140617298738L;
	private List<Token> revoked;


	public RevokedWrapper() {

	}

	public RevokedWrapper(List<Token> revoked) {
		this.revoked = revoked;
	}

	public List<Token> getRevoked() {
		return revoked;
	}

	public void setRevoked(List<Token> revoked) {
		this.revoked = revoked;
	}

}
