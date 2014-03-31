//package com.infinities.keystone4j.assignment.model;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import com.infinities.keystone4j.BaseEntity;
//
//@Entity
//@Table(name = "USER_DOMAIN_GRANT_METADATA", schema = "PUBLIC", catalog = "PUBLIC")
//public class UserDomainGrantMetadata extends BaseEntity implements java.io.Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5727136446409993382L;
//	private UserDomainGrant grant;
//	private Role role;
//
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "GRANTID", nullable = false)
//	public UserDomainGrant getGrant() {
//		return grant;
//	}
//
//	public void setGrant(UserDomainGrant grant) {
//		this.grant = grant;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ROLEID", nullable = false)
//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
// }
