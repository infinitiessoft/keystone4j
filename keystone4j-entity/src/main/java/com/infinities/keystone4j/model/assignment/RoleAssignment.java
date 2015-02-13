package com.infinities.keystone4j.model.assignment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.model.BaseEntity;

@Entity
@Table(name = "ROLE_ASSIGNMENT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"TYPE", "ACTORID", "TARGETID", "ROLEID" }) })
public class RoleAssignment extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static enum AssignmentType {
		USER_PROJECT, GROUP_PROJECT, USER_DOMAIN, GROUP_DOMAIN
	}


	private String actorId;
	private String targetId;
	private Role role;
	private AssignmentType type;
	private Boolean inherited = false;

	private boolean actorIdUpdate = false;
	private boolean targetIdUpdate = false;
	private boolean inheritedUpdate = false;


	@Transient
	public void setActorIdUpdate(boolean actorIdUpdate) {
		this.actorIdUpdate = actorIdUpdate;
	}

	@Transient
	public void setTargetIdUpdate(boolean targetIdUpdate) {
		this.targetIdUpdate = targetIdUpdate;
	}

	@Transient
	public void setInheritedUpdate(boolean inheritedUpdate) {
		this.inheritedUpdate = inheritedUpdate;
	}

	@Transient
	public boolean isTargetIdUpdate() {
		return targetIdUpdate;
	}

	@Transient
	public boolean isInheritedUpdate() {
		return inheritedUpdate;
	}

	@Column(name = "ACTORID", length = 64, nullable = false)
	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
		this.setActorIdUpdate(true);
	}

	@Column(name = "TARGETID", length = 64, nullable = false)
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
		this.setTargetIdUpdate(true);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLEID", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "INHERITED", nullable = false)
	public Boolean getInherited() {
		return inherited;
	}

	public void setInherited(Boolean inherited) {
		this.inherited = inherited;
		this.setInheritedUpdate(true);
	}

	@Transient
	public boolean isActorIdUpdate() {
		return actorIdUpdate;
	}

	@Enumerated(EnumType.STRING)
	public AssignmentType getType() {
		return type;
	}

	public void setType(AssignmentType type) {
		this.type = type;
	}

	@Transient
	@XmlTransient
	public void setRoleId(String roleId) {
		Role role = new Role();
		role.setId(roleId);
		this.setRole(role);
	}

	@Transient
	@XmlTransient
	public String getRoleId() {
		if (this.getRole() == null) {
			return null;
		}
		return this.getRole().getId();
	}
}
