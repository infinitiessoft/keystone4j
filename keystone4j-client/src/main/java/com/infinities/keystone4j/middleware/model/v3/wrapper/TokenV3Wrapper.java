/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware.model.v3.wrapper;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.middleware.model.Bind;
import com.infinities.keystone4j.middleware.model.TokenWrapper;
import com.infinities.keystone4j.middleware.model.v3.Token;

public class TokenV3Wrapper implements Serializable, TokenWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Token token;


	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	@XmlTransient
	public Calendar getExpire() {
		return token.getExpiresAt();
	}

	@Override
	@XmlTransient
	public Bind getBind() {
		return token.getBind();
	}

}
