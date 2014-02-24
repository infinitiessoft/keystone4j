package com.infinities.keystone4j.assignment.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.BaseEntity;

@Entity
@Table(name = "GROUP_PROJECT_GRANT_METADATA", schema = "PUBLIC", catalog = "PUBLIC")
public class UserProjectGrantMetadata extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private UserProjectGrant grant;
	private Role role;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRANTID", nullable = false)
	public UserProjectGrant getGrant() {
		return grant;
	}

	public void setGrant(UserProjectGrant grant) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((grant == null) ? 0 : grant.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProjectGrantMetadata other = (UserProjectGrantMetadata) obj;
		if (grant == null) {
			if (other.grant != null)
				return false;
		} else if (!grant.equals(other.grant))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}
