package com.infinities.keystone4j.model.assignment;

import javax.xml.bind.annotation.XmlElement;

public class RoleAssignment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Links links = new Links();
	private User user;
	private Group group;
	private Role role;
	private Scope scope;


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}


	public class Links {

		private String assignment;
		private String membership;


		public String getAssignment() {
			return assignment;
		}

		public void setAssignment(String assignment) {
			this.assignment = assignment;
		}

		public String getMembership() {
			return membership;
		}

		public void setMembership(String membership) {
			this.membership = membership;
		}

	}

	public static class User {

		private String id;


		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public static class Group {

		private String id;


		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public static class Role {

		private String id;


		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public interface Scope {

		public String getInheritedTo();

		@XmlElement(name = "OS-INHERIT:inherited_to")
		public void setInheritedTo(String inheritedTo);

	}

	public static class ProjectScope implements Scope {

		private Project project = new Project();
		@XmlElement(name = "OS-INHERIT:inherited_to")
		private String inheritedTo;


		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}


		public static class Project {

			private String id;


			public String getId() {
				return this.id;
			}

			public void setId(String id) {
				this.id = id;
			}
		}


		@Override
		public String getInheritedTo() {
			return inheritedTo;
		}

		@Override
		public void setInheritedTo(String inheritedTo) {
			this.inheritedTo = inheritedTo;
		}

	}

	public static class DomainScope implements Scope {

		private Domain domain = new Domain();
		@XmlElement(name = "OS-INHERIT:inherited_to")
		private String inheritedTo;


		public Domain getDomain() {
			return domain;
		}

		public void setDomain(Domain domain) {
			this.domain = domain;
		}


		public static class Domain {

			private String id;


			public String getId() {
				return this.id;
			}

			public void setId(String id) {
				this.id = id;
			}
		}


		@Override
		public String getInheritedTo() {
			return inheritedTo;
		}

		@Override
		public void setInheritedTo(String inheritedTo) {
			this.inheritedTo = inheritedTo;
		}

	}
}
