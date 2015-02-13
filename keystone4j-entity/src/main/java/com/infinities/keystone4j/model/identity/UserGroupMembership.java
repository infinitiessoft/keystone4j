package com.infinities.keystone4j.model.identity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.infinities.keystone4j.model.BaseEntity;

@Entity
@Table(name = "USER_GROUP_MEMBERSHIP", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"USERID", "GROUPID" }) })
public class UserGroupMembership extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316399395123858269L;
	private User user; // keystone.identity.backends.sql.UserGroupMembership
						// 20150114
	private Group group; // keystone.identity.backends.sql.UserGroupMembership
							// 20150114


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
