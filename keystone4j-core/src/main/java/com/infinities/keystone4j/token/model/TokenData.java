package com.infinities.keystone4j.token.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.ISO8601DateAdapter;
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.catalog.model.Catalog;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.trust.model.Trust;

public class TokenData {

	private List<String> methods = new ArrayList<String>(0);
	private String extras;
	private List<Role> roles = new ArrayList<Role>(0);
	private User user;
	private Catalog catalog;
	private Project project;
	private Domain domain;
	@XmlElement(name = "OS-TRUST:trust")
	private Trust trust;
	@XmlJavaTypeAdapter(value = ISO8601DateAdapter.class, type = Date.class)
	@XmlElement(name = "expires_at")
	private Date expireAt;
	@XmlJavaTypeAdapter(value = ISO8601DateAdapter.class, type = Date.class)
	@XmlElement(name = "issued_at")
	private Date issuedAt;
	private Token token;
	private Bind bind;


	@JsonView(Views.AuthenticateForToken.class)
	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	@XmlTransient
	public String getExtras() {
		return extras;
	}

	@XmlTransient
	public void setExtras(String extras) {
		this.extras = extras;
	}

	@JsonView(Views.AuthenticateForToken.class)
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@JsonView(Views.AuthenticateForToken.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonView(Views.AuthenticateForToken.class)
	@XmlElement()
	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@JsonView(Views.AuthenticateForToken.class)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@JsonView(Views.AuthenticateForToken.class)
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

	@JsonView(Views.AuthenticateForToken.class)
	public Date getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Date expireAt) {
		this.expireAt = expireAt;
	}

	@JsonView(Views.AuthenticateForToken.class)
	public Date getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

}
