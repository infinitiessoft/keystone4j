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
package com.infinities.keystone4j.middleware.model.wrapper;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinities.keystone4j.middleware.model.Access;
import com.infinities.keystone4j.middleware.model.Bind;
import com.infinities.keystone4j.middleware.model.TokenWrapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessWrapper implements Serializable, TokenWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Access access;


	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	@Override
	@XmlTransient
	public Calendar getExpire() {
		return access.getToken().getExpires();
	}

	@Override
	@XmlTransient
	public Bind getBind() {
		return access.getToken().getBind();
	}

}
