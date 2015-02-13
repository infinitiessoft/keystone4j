package com.infinities.keystone4j.model.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Metadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Role> roles = new ArrayList<Role>();


	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	public static class Role implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String id;
		@XmlElement(name = "inherited_to")
		private String iheritedTo;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIheritedTo() {
			return iheritedTo;
		}

		public void setIheritedTo(String iheritedTo) {
			this.iheritedTo = iheritedTo;
		}

	}

}
