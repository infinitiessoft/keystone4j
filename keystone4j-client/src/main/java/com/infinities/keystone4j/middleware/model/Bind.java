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
package com.infinities.keystone4j.middleware.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinities.keystone4j.middleware.model.bind.Kerberos;
import com.infinities.keystone4j.middleware.model.bind.X509;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Kerberos kerberos;
	private X509 x590;


	public Kerberos getKerberos() {
		return kerberos;
	}

	public void setKerberos(Kerberos kerberos) {
		this.kerberos = kerberos;
	}

	public X509 getX590() {
		return x590;
	}

	public void setX590(X509 x590) {
		this.x590 = x590;
	}

	public boolean isEmpty() {
		return kerberos == null && x590 == null;
	}

}
