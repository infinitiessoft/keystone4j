package com.infinities.keystone4j.trust.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.role.v3.AbstractRoleAction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class AbstractTrustAction<T extends BaseEntity> extends AbstractAction<T> {

	protected AssignmentApi assignmentApi;
	protected IdentityApi identityApi;
	protected TokenProviderApi tokenProviderApi;
	protected TrustApi trustApi;


	public AbstractTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public void setTrustApi(TrustApi trustApi) {
		this.trustApi = trustApi;
	}

	@Override
	public String getCollectionName() {
		return "trusts";
	}

	@Override
	public String getMemberName() {
		return "trust";
	}

	@Override
	protected String getBaseUrl(ContainerRequestContext context, String path) {
		path = "/OS-TRUST/" + this.getCollectionName();
		return super.getBaseUrl(context, path);
	}

	protected void fillInRoles(ContainerRequestContext request, Trust trust, List<Role> allRoles) {
		Set<Role> trustFullRoles = Sets.newHashSet();
		AbstractRoleAction roleV3 = new AbstractRoleAction(assignmentApi, tokenProviderApi, policyApi) {

			@Override
			public String getName() {
				return null;
			}

		};

		for (Role trustRole : trust.getRoles()) {
			List<Role> matchingRoles = Lists.newArrayList();

			for (Role role : allRoles) {
				if (role.getId().equals(trustRole.getId())) {
					matchingRoles.add(role);
				}
			}
			if (!matchingRoles.isEmpty()) {
				Role fullRole = ((RoleWrapper) roleV3.wrapMember(request, matchingRoles.get(0))).getRef();
				trustFullRoles.add(fullRole);
			}
		}
		trust.setRoles(new ArrayList<Role>(trustFullRoles));
		trust.getRolesLinks().setSelf(getBaseUrl(request, null) + String.format("/%s/roles", trust.getId()));

	}

	protected String getUserId(KeystoneContext context) throws Exception {
		if (!Strings.isNullOrEmpty(context.getTokenid())) {
			String tokenid = context.getTokenid();
			ITokenDataWrapper tokenData = tokenProviderApi.validateToken(tokenid, null);
			KeystoneToken tokenRef = new KeystoneToken(tokenid, tokenData);
			return tokenRef.getUserId();
		}
		return null;
	}

	protected void adminTrustorOnly(KeystoneContext context, Trust trust, String userid) {
		if (!userid.equals(trust.getTrustorUserId()) && !context.isAdmin()) {
			throw Exceptions.ForbiddenException.getInstance();
		}
	}

	protected void trustorTrusteeOnly(Trust trust, String userid) {
		if (!userid.equals(trust.getTrusteeUserId()) && !userid.equals(trust.getTrustorUserId())) {
			throw Exceptions.ForbiddenException.getInstance();
		}
	}

	protected void checkRoleForTrust(ContainerRequestContext request, String trustid, String roleid) throws Exception {
		Trust trust = this.getTrustApi().getTrust(trustid, false);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String userid = getUserId(context);
		trustorTrusteeOnly(trust, userid);

		for (Role role : trust.getRoles()) {
			if (roleid.equals(role.getId())) {
				return;
			}
		}

		throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
	}

	protected Trust getTrust(ContainerRequestContext request, String trustid) throws Exception {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String userid = getUserId(context);
		Trust trust = this.getTrustApi().getTrust(trustid, false);
		if (trust != null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}
		trustorTrusteeOnly(trust, userid);
		fillInRoles(request, trust, this.getAssignmentApi().listRoles(null));
		return trust;
	}

}
