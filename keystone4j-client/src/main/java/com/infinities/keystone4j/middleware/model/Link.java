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

public class Link implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rel;

	private String href;

	private String type;


	/**
	 * @return the rel
	 */
	public String getRel() {
		return rel;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Link [rel=" + rel + ", href=" + href + ", type=" + type + "]";
	}

}
