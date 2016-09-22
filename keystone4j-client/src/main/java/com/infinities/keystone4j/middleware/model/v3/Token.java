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
package com.infinities.keystone4j.middleware.model.v3;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinities.keystone4j.middleware.model.Bind;

@XmlRootElement(name = "token")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	@XmlElement(name = "expires_at")
	private Calendar expiresAt;

	@XmlElement(name = "issued_at")
	private Calendar issuedAt;

	private List<String> methods;

	private Bind bind;

	private String regionName;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Domain implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String id;

		private String name;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}


	private Domain domain;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Project implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Domain domain;

		private String id;

		private String name;


		public Domain getDomain() {
			return domain;
		}

		public void setDomain(Domain domain) {
			this.domain = domain;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}


	private Project project;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class User implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class Domain implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private String id;

			private String name;


			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

		}


		private String id;

		private String name;

		private Domain domain;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Domain getDomain() {
			return domain;
		}

		public void setDomain(Domain domain) {
			this.domain = domain;
		}

	}


	private User user;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Role implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String id;

		private String name;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}


	private List<Role> roles;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Service implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class Endpoint implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private String id;

			private String url;

			private String region;

			private Boolean enabled;

			@XmlElement(name = "legacy_endpoint_id")
			private String legacyEndpointId;

			@XmlElement(name = "interface")
			private String iface;


			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getRegion() {
				return region;
			}

			public void setRegion(String region) {
				this.region = region;
			}

			public Boolean getEnabled() {
				return enabled;
			}

			public void setEnabled(Boolean enabled) {
				this.enabled = enabled;
			}

			public String getLegacyEndpointId() {
				return legacyEndpointId;
			}

			public void setLegacyEndpointId(String legacyEndpointId) {
				this.legacyEndpointId = legacyEndpointId;
			}

			public String getInterface() {
				return iface;
			}

			public void setInterface(String iface) {
				this.iface = iface;
			}

		}


		private String name;

		private String id;

		private String type;

		private List<Endpoint> endpoints;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Endpoint> getEndpoints() {
			return endpoints;
		}

		public void setEndpoints(List<Endpoint> endpoints) {
			this.endpoints = endpoints;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static final class Trust implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String id;

		@XmlElement(name = "trustee_user")
		private User trustee;

		@XmlElement(name = "trustor_user")
		private User trustor;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public User getTrustee() {
			return trustee;
		}

		public void setTrustee(User trustee) {
			this.trustee = trustee;
		}

		public User getTrustor() {
			return trustor;
		}

		public void setTrustor(User trustor) {
			this.trustor = trustor;
		}

	}

	public static final class Oauth implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@XmlElement(name = "access_token_id")
		private String accessTokenId;
		@XmlElement(name = "consumer_id")
		private String consumerId;


		public String getAccessTokenId() {
			return accessTokenId;
		}

		public void setAccessTokenId(String accessTokenId) {
			this.accessTokenId = accessTokenId;
		}

		public String getConsumerId() {
			return consumerId;
		}

		public void setConsumerId(String consumerId) {
			this.consumerId = consumerId;
		}

	}


	@XmlElement(name = "OS-OAUTH1")
	private Oauth oauth1;

	@XmlElement(name = "OS-TRUST:trust")
	private Trust trust;

	private List<Service> catalog;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Calendar expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Calendar getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Calendar issuedAt) {
		this.issuedAt = issuedAt;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Service> getCatalog() {
		return catalog;
	}

	public void setCatalog(List<Service> catalog) {
		this.catalog = catalog;
	}

	public Bind getBind() {
		return bind;
	}

	public void setBind(Bind bind) {
		this.bind = bind;
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", expiresAt=" + expiresAt + ", issuedAt=" + issuedAt + ", methods=" + methods
				+ ", domain=" + domain + ", project=" + project + ", user=" + user + ", roles=" + roles + ", catalog="
				+ catalog + "]";
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	public Oauth getOauth1() {
		return oauth1;
	}

	public void setOauth1(Oauth oauth1) {
		this.oauth1 = oauth1;
	}

}
