package com.infinities.keystone4j.model.token.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinities.keystone4j.model.common.Link;

@XmlRootElement(name = "access")
public class Access implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final class Service {

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class Endpoint {

			private String region;

			private String publicURL;

			private String internalURL;

			private String adminURL;


			/**
			 * @return the region
			 */
			public String getRegion() {
				return region;
			}

			/**
			 * @return the publicURL
			 */
			public String getPublicURL() {
				return publicURL;
			}

			/**
			 * @return the internalURL
			 */
			public String getInternalURL() {
				return internalURL;
			}

			/**
			 * @return the adminURL
			 */
			public String getAdminURL() {
				return adminURL;
			}

			public void setRegion(String region) {
				this.region = region;
			}

			public void setPublicURL(String publicURL) {
				this.publicURL = publicURL;
			}

			public void setInternalURL(String internalURL) {
				this.internalURL = internalURL;
			}

			public void setAdminURL(String adminURL) {
				this.adminURL = adminURL;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "Endpoint [region=" + region + ", publicURL=" + publicURL + ", internalURL=" + internalURL
						+ ", adminURL=" + adminURL + "]";
			}

		}


		private String type;

		private String name;

		private List<Endpoint> endpoints = new ArrayList<Endpoint>(0);

		@XmlElement(name = "endpoints_links")
		private List<Link> endpointsLinks = new ArrayList<Link>(0);


		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the endpoints
		 */
		public List<Endpoint> getEndpoints() {
			return endpoints;
		}

		/**
		 * @return the endpointsLinks
		 */
		public List<Link> getEndpointsLinks() {
			return endpointsLinks;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setEndpoints(List<Endpoint> endpoints) {
			this.endpoints = endpoints;
		}

		public void setEndpointsLinks(List<Link> endpointsLinks) {
			this.endpointsLinks = endpointsLinks;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Service [type=" + type + ", name=" + name + ", endpoints=" + endpoints + ", endpointsLinks="
					+ endpointsLinks + "]";
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class User {

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class Role {

			private String id;

			private String name;


			/**
			 * @return the id
			 */
			public String getId() {
				return id;
			}

			/**
			 * @return the name
			 */
			public String getName() {
				return name;
			}

			public void setId(String id) {
				this.id = id;
			}

			public void setName(String name) {
				this.name = name;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "Role [id=" + id + ", name=" + name + "]";
			}

		}


		private String id;

		private String name;

		private String username;

		private List<Role> roles = new ArrayList<Role>();

		@XmlElement(name = "roles_links")
		private List<Link> rolesLinks = new ArrayList<Link>(0);

		private String tenantId;


		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @return the roles
		 */
		public List<Role> getRoles() {
			return roles;
		}

		/**
		 * @return the rolesLinks
		 */
		public List<Link> getRolesLinks() {
			return rolesLinks;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}

		public void setRolesLinks(List<Link> rolesLinks) {
			this.rolesLinks = rolesLinks;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", username=" + username + ", roles=" + roles + ", rolesLinks="
					+ rolesLinks + "]";
		}

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Trust {

		private String id;
		@XmlElement(name = "trustee_user_id")
		private String trusteeUserId;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTrusteeUserId() {
			return trusteeUserId;
		}

		public void setTrusteeUserId(String trusteeUserId) {
			this.trusteeUserId = trusteeUserId;
		}

	}


	private Trust trust;

	private TokenV2 token;

	private List<Service> serviceCatalog = new ArrayList<Service>(0);

	private User user;

	private Metadata metadata;

	private String regionName;


	/**
	 * @return the token
	 */
	public TokenV2 getToken() {
		return token;
	}

	/**
	 * @return the serviceCatalog
	 */
	public List<Service> getServiceCatalog() {
		return serviceCatalog;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Metadata {

		@XmlElement(name = "is_admin")
		private int isAdmin;
		@XmlElement(name = "roles")
		private List<String> roles = new ArrayList<String>();


		public int getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(int isAdmin) {
			this.isAdmin = isAdmin;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

	}


	/**
	 * @return the metadata
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


	private String tenantId;


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setToken(TokenV2 token) {
		this.token = token;
	}

	public void setServiceCatalog(List<Service> serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Access [token=" + token + ", serviceCatalog=" + serviceCatalog + ", user=" + user + ", metadata=" + metadata
				+ "]";
	}

}