package com.infinities.keystone4j.model.token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.utils.ISO8601DateAdapter;
import com.infinities.keystone4j.model.utils.Views;

public class TokenData implements ITokenData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Service> catalog;
	@XmlJavaTypeAdapter(value = ISO8601DateAdapter.class, type = Date.class)
	@XmlElement(name = "expires_at")
	private Calendar expireAt;
	private Map<String, String> extras = new HashMap<String, String>(); // check
	@XmlJavaTypeAdapter(value = ISO8601DateAdapter.class, type = Date.class)
	@XmlElement(name = "issued_at")
	private Calendar issuedAt;
	private List<String> methods = new ArrayList<String>(0); // check
	private Project project;
	private List<Role> roles = new ArrayList<Role>(0);
	private User user;
	private Domain domain;
	@XmlElement(name = "OS-TRUST:trust")
	private Trust trust;

	private Token token;
	private Bind bind;
	private List<String> auditIds = new ArrayList<String>(0);


	@JsonView(Views.Basic.class)
	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	@JsonView(Views.Basic.class)
	public Map<String, String> getExtras() {
		return extras;
	}

	@XmlTransient
	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}

	@JsonView(Views.Basic.class)
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	@JsonView(Views.Basic.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonView(Views.Basic.class)
	public List<Service> getCatalog() {
		return catalog;
	}

	public void setCatalog(List<Service> catalog) {
		this.catalog = catalog;
	}

	@JsonView(Views.Basic.class)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@JsonView(Views.Basic.class)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@XmlTransient
	public Token getToken() {
		return token;
	}

	@XmlTransient
	public void setToken(Token token) {
		this.token = token;
	}

	@XmlTransient
	public Bind getBind() {
		return bind;
	}

	@XmlTransient
	public void setBind(Bind bind) {
		this.bind = bind;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	@JsonView(Views.Basic.class)
	public Calendar getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Calendar expireAt) {
		this.expireAt = expireAt;
	}

	@JsonView(Views.Basic.class)
	public Calendar getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Calendar issuedAt) {
		this.issuedAt = issuedAt;
	}


	public static class Trust implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String id;
		@XmlElement(name = "trustor_user")
		private User trustorUser;
		@XmlElement(name = "trustee_user")
		private User trusteeUser;
		private Boolean impersonation;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public User getTrustorUser() {
			return trustorUser;
		}

		public void setTrustorUser(User trustorUser) {
			this.trustorUser = trustorUser;
		}

		public User getTrusteeUser() {
			return trusteeUser;
		}

		public void setTrusteeUser(User trusteeUser) {
			this.trusteeUser = trusteeUser;
		}

		public Boolean getImpersonation() {
			return impersonation;
		}

		public void setImpersonation(Boolean impersonation) {
			this.impersonation = impersonation;
		}

	}


	// public static class User implements Serializable {
	//
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	// private String id;
	// private String name;
	// private Domain domain;
	//
	//
	// public String getId() {
	// return id;
	// }
	//
	// public void setId(String id) {
	// this.id = id;
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public Domain getDomain() {
	// return domain;
	// }
	//
	// public void setDomain(Domain domain) {
	// this.domain = domain;
	// }
	//
	// }

	public List<String> getAuditIds() {
		return auditIds;
	}

	public void setAuditIds(List<String> auditIds) {
		this.auditIds = auditIds;
	}

}
