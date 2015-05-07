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
package com.infinities.keystone4j.model.catalog.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class ServicesWrapper implements CollectionWrapper<Service> {

	private List<Service> services;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public ServicesWrapper() {

	}

	public ServicesWrapper(List<Service> services) {
		this.services = services;
	}

	@JsonView(Views.Advance.class)
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
	public void setRefs(List<Service> refs) {
		this.services = refs;
	}

	@JsonView(Views.Advance.class)
	@XmlElement(name = "services")
	public List<Service> getRefs() {
		return services;
	}
}
