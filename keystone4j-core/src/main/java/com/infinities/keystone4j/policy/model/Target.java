package com.infinities.keystone4j.policy.model;

import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.model.Token;

public class Target {

	private Token token;
	private User user;
	private Project project;
	private Group group;
	private Domain domain;
	private String url;


	public Target(Token token, User user, Project project, Group group, Domain domain, String url) {
		super();
		this.token = token;
		this.user = user;
		this.project = project;
		this.group = group;
		this.domain = domain;
		this.url = url;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
