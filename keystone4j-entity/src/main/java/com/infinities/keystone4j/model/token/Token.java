package com.infinities.keystone4j.model.token;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.google.common.base.Strings;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;

@Entity
@Table(name = "TOKEN", schema = "PUBLIC", catalog = "PUBLIC")
public class Token implements java.io.Serializable, IToken, AuthData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 899631282574098673L;
	private String id;
	private Calendar expires;
	private String userId;
	private String trustId;
	private String extra;
	private Boolean valid = true;
	private Bind bind;

	private int version;
	// from extra
	private String tokenVersion;
	private Metadata metadata = new Metadata();
	private String projectId;
	private ITokenDataWrapper tokenData;

	private byte[] tokenDataBytes;


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
	@Column(name = "ID", unique = true, nullable = false, length = 65535)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRES")
	public Calendar getExpires() {
		return expires;
	}

	public void setExpires(Calendar expires) {
		this.expires = expires;
	}

	@Lob
	@Column(name = "EXTRA", nullable = true)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Column(name = "VALID", nullable = false)
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	@Override
	@Transient
	public Bind getBind() {
		return bind;
	}

	@Transient
	public void setBind(Bind bind) {
		this.bind = bind;
	}

	@Override
	@OneToOne(optional = false, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "METADATA_ID", unique = true, nullable = false)
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	@Column(name = "USERID", length = 64)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "TRUSTID", length = 64)
	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	@Column(name = "TOKEN_VERSION", length = 5)
	public String getTokenVersion() {
		return tokenVersion;
	}

	public void setTokenVersion(String tokenVersion) {
		this.tokenVersion = tokenVersion;
	}

	@Column(name = "PROJECTID", length = 64)
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	// from Data
	@Override
	@Transient
	public User getUser() {
		if (Strings.isNullOrEmpty(getUserId())) {
			return null;
		}
		User user = new User();
		user.setId(getUserId());
		return user;
	}

	// from Data
	@Override
	@Transient
	public Project getTenant() {
		if (Strings.isNullOrEmpty(getProjectId())) {
			return null;
		}
		Project project = new Project();
		project.setId(getProjectId());
		return project;
	}

	public void setTenant(Project tenant) {
		if (tenant != null) {
			this.projectId = tenant.getId();
		}
	}

	// @XmlTransient
	// @Transient
	// public TokenDataWrapper getTokenData() {
	// TokenData tokenData = new TokenData();
	// tokenData.setBind(getBind());
	// tokenData.setUser(getUser());
	// tokenData.setProject(getProject());
	// tokenData.setTrust(getTrust());
	// tokenData.setExpireAt(getExpires());
	// tokenData.setIssuedAt(getIssueAt());
	// tokenData.setDomain(getDomain());
	//
	// for (TokenRole tokenRole : getTokenRoles()) {
	// tokenData.getRoles().add(tokenRole.getRole());
	// }
	//
	// return new TokenDataWrapper(tokenData);
	// }
	//
	// @Transient
	// public void setTokenData(TokenDataWrapper tokenData) {
	// setBind(tokenData.getToken().getBind());
	// setUser(tokenData.getToken().getUser());
	// setProject(tokenData.getToken().getProject());
	// setTrust(tokenData.getToken().getTrust());
	// setExpires(tokenData.getToken().getExpireAt());
	// setIssueAt(tokenData.getToken().getIssuedAt());
	// setDomain(tokenData.getToken().getDomain());
	//
	// for (Role role : tokenData.getToken().getRoles()) {
	// this.getTokenRoles().add(new TokenRole(this, role));
	// }
	// }

	@Transient
	@Override
	public ITokenDataWrapper getTokenData() {
		return tokenData;
	}

	public void setTokenData(ITokenDataWrapper tokenData) {
		this.tokenData = tokenData;
	}

	@Transient
	@Override
	public String getParentAuditId() {
		return null;
	}

	@Lob
	@Column(name = "TOKENDATABYTES")
	public byte[] getTokenDataBytes() {
		return tokenDataBytes;
	}

	public void setTokenDataBytes(byte[] tokenDataBytes) {
		this.tokenDataBytes = tokenDataBytes;
	}
}
