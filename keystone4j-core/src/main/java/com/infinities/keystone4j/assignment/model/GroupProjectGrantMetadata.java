package com.infinities.keystone4j.assignment.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.BaseEntity;

@Entity
@Table(name = "GROUP_PROJECT_GRANT_METADATA", schema = "PUBLIC", catalog = "PUBLIC")
public class GroupProjectGrantMetadata extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private GroupProjectGrant grant;
	private Role role;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRANTID", nullable = false)
	public GroupProjectGrant getGrant() {
		return grant;
	}

	public void setGrant(GroupProjectGrant grant) {
		this.grant = grant;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLEID", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
