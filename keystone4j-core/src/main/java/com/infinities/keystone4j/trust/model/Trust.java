package com.infinities.keystone4j.trust.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.model.Token;

@Entity
@Table(name = "TRUST", schema = "PUBLIC", catalog = "PUBLIC")
public class Trust extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3573037726561198048L;
	@XmlElement(name = "trustor_user")
	private User trustor;
	@XmlElement(name = "trustee_user")
	private User trustee;
	private Project project;
	private Boolean impersonation;
	private Date deletedAt;
	private Date expiresAt;
	private String extra;
	private Set<TrustRole> trustRoles = new HashSet<TrustRole>(0);
	private Set<Token> tokens = new HashSet<Token>(0);


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRUSTORID", nullable = false)
	public User getTrustor() {
		return trustor;
	}

	public void setTrustor(User trustor) {
		this.trustor = trustor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRUSTEEID", nullable = false)
	public User getTrustee() {
		return trustee;
	}

	public void setTrustee(User trustee) {
		this.trustee = trustee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "IMPERSONATION")
	public Boolean getImpersonation() {
		return impersonation;
	}

	public void setImpersonation(Boolean impersonation) {
		this.impersonation = impersonation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETEAT")
	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIREAT")
	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trust", cascade = CascadeType.ALL)
	public Set<TrustRole> getTrustRoles() {
		return trustRoles;
	}

	public void setTrustRoles(Set<TrustRole> trustRoles) {
		this.trustRoles = trustRoles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trust", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}

}
