package com.infinities.keystone4j.token.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.auth.model.AuthData;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.trust.model.Trust;

@Entity
@Table(name = "TOKEN", schema = "PUBLIC", catalog = "PUBLIC")
public class Token implements java.io.Serializable, PolicyEntity, AuthData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 899631282574098673L;
	private Date expires = new Date();
	private Date issueAt = new Date();
	private String extra;
	private Boolean valid = true;
	private User user;
	private Project project;
	private Domain domain;
	private Trust trust;
	private Bind bind;
	private String id;
	private int version;
	private Set<TokenRole> tokenRoles = new HashSet<TokenRole>(0);


	@Version
	@Column(name = "OPTLOCK")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	@Id
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRES")
	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ISSUEAT")
	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	@Lob
	@Column(name = "EXTRA", nullable = false)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Column(name = "VALID")
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = true)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = true)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRUSTID", nullable = false)
	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	@Transient
	public Bind getBind() {
		return bind;
	}

	@Transient
	public void setBind(Bind bind) {
		this.bind = bind;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "token", cascade = CascadeType.ALL)
	public Set<TokenRole> getTokenRoles() {
		return tokenRoles;
	}

	public void setTokenRoles(Set<TokenRole> tokenRoles) {
		this.tokenRoles = tokenRoles;
	}

	@XmlTransient
	@Transient
	public TokenDataWrapper getTokenData() {
		TokenData tokenData = new TokenData();
		tokenData.setBind(getBind());
		tokenData.setUser(getUser());
		tokenData.setProject(getProject());
		tokenData.setTrust(getTrust());
		tokenData.setExpireAt(getExpires());
		tokenData.setIssuedAt(getIssueAt());
		tokenData.setDomain(getDomain());

		for (TokenRole tokenRole : getTokenRoles()) {
			tokenData.getRoles().add(tokenRole.getRole());
		}

		return new TokenDataWrapper(tokenData);
	}

	@Transient
	public void setTokenData(TokenDataWrapper tokenData) {
		setBind(tokenData.getToken().getBind());
		setUser(tokenData.getToken().getUser());
		setProject(tokenData.getToken().getProject());
		setTrust(tokenData.getToken().getTrust());
		setExpires(tokenData.getToken().getExpireAt());
		setIssueAt(tokenData.getToken().getIssuedAt());
		setDomain(tokenData.getToken().getDomain());

		for (Role role : tokenData.getToken().getRoles()) {
			this.getTokenRoles().add(new TokenRole(this, role));
		}
	}
}
