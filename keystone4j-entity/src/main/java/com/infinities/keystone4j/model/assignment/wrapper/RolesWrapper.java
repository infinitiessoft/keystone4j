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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class RolesWrapper implements CollectionWrapper<Role> {

	private List<Role> roles;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public RolesWrapper() {

	}

	public RolesWrapper(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
		this.links = links;
	}

	@JsonView(Views.All.class)
	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Role> refs) {
		this.roles = refs;
	}

	@XmlElement(name = "roles")
	public List<Role> getRefs() {
		return roles;
	}

}