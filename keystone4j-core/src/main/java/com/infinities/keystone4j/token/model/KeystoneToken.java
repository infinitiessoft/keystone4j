package com.infinities.keystone4j.token.model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.DecoderException;

import com.google.common.collect.Iterables;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.ITokenData;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.utils.Cms;

//keystone.models.token_model.KeystoneToken
public class KeystoneToken {

	private final ITokenData tokenData;
	private final String version;
	private final String tokenid;
	private final String shortid;
	public static final String V2 = "v2.0";
	public static final String V3 = "v3.0";

	public static final Set<String> VERSIONS;

	static {
		Set<String> s = new HashSet<String>();
		s.add(V2);
		s.add(V3);
		VERSIONS = Collections.unmodifiableSet(s);
	}


	public KeystoneToken(String tokenid, ITokenDataWrapper token) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		if (token instanceof TokenV2DataWrapper) {
			tokenData = ((TokenV2DataWrapper) token).getAccess();
			version = V2;
		} else if (token instanceof TokenDataWrapper) {
			tokenData = ((TokenDataWrapper) token).getToken();
			version = V3;
		} else {
			throw Exceptions.UnsupportedTokenVersionException.getInstance();
		}
		this.tokenid = tokenid;
		this.shortid = Cms.Instance.hashToken(tokenid,
				Cms.Algorithm.valueOf(Config.Instance.getOpt(Config.Type.token, "hash_algorithm").asText()));

		if (this.isProjectScoped() && this.isDomainScoped()) {
			throw Exceptions.UnexpectedException.getInstance("Found invalid token: scoped to both project and domain.");
		}

	}

	public Calendar getExpires() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getExpireAt();
		} else {
			return ((Access) tokenData).getToken().getExpires();
		}
	}

	public Calendar getIssued() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getIssuedAt();
		} else {
			return ((Access) tokenData).getToken().getIssued_at();
		}
	}

	public String getAutitId() {
		if (V3.equals(version)) {
			return Iterables.getFirst(((TokenData) tokenData).getAuditIds(), null);
		} else {
			return Iterables.getFirst(((Access) tokenData).getToken().getAuditIds(), null);
		}
	}

	public String getAutitChainId() {
		if (V3.equals(version)) {
			return Iterables.getLast(((TokenData) tokenData).getAuditIds(), null);
		} else {
			return Iterables.getLast(((Access) tokenData).getToken().getAuditIds(), null);
		}
	}

	public String getAuthToken() {
		return tokenid;
	}

	public String getUserId() {
		return tokenData.getUser().getId();
	}

	public String getUserName() {
		return tokenData.getUser().getName();
	}

	public String getUserDomainName() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getUser().getDomain().getName();
			} else if (tokenData.getUser() != null) {
				return "Default";
			}
		} catch (Exception e) {
		}
		throw Exceptions.UnexpectedException.getInstance();
	}

	public String getUserDomainId() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getUser().getDomain().getId();
			} else if (tokenData.getUser() != null) {
				return Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
			}
		} catch (Exception e) {

		}
		throw Exceptions.UnexpectedException.getInstance();
	}

	public String getDomainId() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getDomain().getId();
			}
		} catch (Exception e) {
			throw Exceptions.UnexpectedException.getInstance();
		}
		throw Exceptions.NotImplementedException.getInstance();
	}

	public String getDomainName() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getDomain().getName();
			}
		} catch (Exception e) {
			throw Exceptions.UnexpectedException.getInstance();
		}
		throw Exceptions.NotImplementedException.getInstance();
	}

	public String getProjectId() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getProject().getId();
			} else {
				return ((Access) tokenData).getToken().getTenant().getId();
			}
		} catch (Exception e) {
			throw Exceptions.UnexpectedException.getInstance();
		}
	}

	public String getProjectName() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getProject().getName();
			} else {
				return ((Access) tokenData).getToken().getTenant().getName();
			}
		} catch (Exception e) {
			throw Exceptions.UnexpectedException.getInstance();
		}
	}

	public String getProjectDomainId() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getProject().getDomain().getId();
			} else if (((Access) tokenData).getToken().getTenant() != null) {
				return Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
			}
		} catch (Exception e) {

		}
		throw Exceptions.UnexpectedException.getInstance();
	}

	public String getProjectDomainName() {
		try {
			if (V3.equals(version)) {
				return ((TokenData) tokenData).getProject().getDomain().getId();
			} else if (((Access) tokenData).getToken().getTenant() != null) {
				return Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
			}
		} catch (Exception e) {

		}
		throw Exceptions.UnexpectedException.getInstance();
	}

	public boolean isProjectScoped() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getProject() != null;
		} else {
			return ((Access) tokenData).getToken().getTenant() != null;
		}
	}

	public boolean isDomainScoped() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getDomain() != null;
		}
		return false;
	}

	public boolean isScoped() {
		return this.isProjectScoped() || this.isDomainScoped();
	}

	public String getTrustId() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getTrust().getId();
		} else {
			return ((Access) tokenData).getTrust().getId();
		}
	}

	public String getTrusteeUserId() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getTrust().getTrusteeUser().getId();
		} else {
			return ((Access) tokenData).getTrust().getTrusteeUserId();
		}
	}

	public String getTrustorUserId() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getTrust().getTrustorUser().getId();
		} else {
			return ((Access) tokenData).getTrust().getTrustorUserId();
		}
	}

	public boolean isTrustImpersonation() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getTrust().getImpersonation();
		} else {
			return ((Access) tokenData).getTrust().getImpersonation();
		}
	}

	public boolean isTrustScoped() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getTrust() != null;
		} else {
			return ((Access) tokenData).getTrust() != null;
		}
	}

	public boolean isOauthScoped() {
		// TODO oauth is unimplemented
		return false;
	}

	public String getOauthAccessTokenId() {
		// TODO oauth is unimplemented
		return null;
	}

	public String getOauthConsumerId() {
		// TODO oauth is unimplemented
		return null;
	}

	public List<String> getRoleIds() {
		if (V3.equals(version)) {
			List<String> roleIds = new ArrayList<String>();
			for (Role role : ((TokenData) tokenData).getRoles()) {
				roleIds.add(role.getId());
			}
			return roleIds;
		} else {
			return ((Access) tokenData).getMetadata().getRoles();
		}
	}

	public List<String> getRoleNames() {
		if (V3.equals(version)) {
			List<String> roleNames = new ArrayList<String>();
			for (Role role : ((TokenData) tokenData).getRoles()) {
				roleNames.add(role.getName());
			}
			return roleNames;
		} else {
			List<String> roleNames = new ArrayList<String>();
			for (com.infinities.keystone4j.model.token.v2.Access.User.Role role : ((Access) tokenData).getUser().getRoles()) {
				roleNames.add(role.getName());
			}
			return roleNames;
		}
	}

	public Bind getBind() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getBind();
		} else {
			return ((Access) tokenData).getToken().getBind();
		}
	}

	public boolean isFederatedUser() {
		// TODO federated is unimplemented
		return false;
	}

	public List<String> getFederatedGroupIds() {
		// TODO federated is unimplemented
		return new ArrayList<String>();
	}

	public String getFederatedIdpId() {
		// TODO federated is unimplemented
		return null;
	}

	public String getFederatedProtocolId() {
		// TODO federated is unimplemented
		return null;
	}

	public Metadata getMetadata() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getToken().getMetadata();
		} else {
			return ((Access) tokenData).getMetadata();
		}
	}

	public List<String> getMethods() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getMethods();
		} else {
			return new ArrayList<String>(0);
		}
	}

	public String getShortid() {
		return this.shortid;
	}

	public List<String> getAuditIds() {
		if (V3.equals(version)) {
			return ((TokenData) tokenData).getAuditIds();
		} else {
			return ((Access) tokenData).getToken().getAuditIds();
		}
	}

}
