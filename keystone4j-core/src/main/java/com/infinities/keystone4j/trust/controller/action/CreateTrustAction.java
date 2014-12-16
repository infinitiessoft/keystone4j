package com.infinities.keystone4j.trust.controller.action;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.DecoderException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class CreateTrustAction extends AbstractTrustAction implements ProtectedAction<Trust> {

	private final static String TRUST = "trust";
	private final static String REQUEST = "request";
	private final static String ROLES = "roles";
	private final static String ID_OR_NAME = "id or name";
	private final static String ROLE_NOT_FOUND = "role %s is not defined";
	private final Trust trust;


	public CreateTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, Trust trust) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trust = trust;
	}

	@Override
	public MemberWrapper<Trust> execute(ContainerRequestContext request) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		AuthContext authContext = new AuthContext();
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);

		if (request.getPropertyNames().contains(Authorization.AUTH_CONTEXT_ENV)) {
			authContext = (AuthContext) request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		}
		if (authContext.isDelegatedAuth()) {
			throw Exceptions.ForbiddenException.getInstance("Cannot create a trust with a token issues via delegation.");
		}

		if (trust == null) {
			throw Exceptions.ValidationException.getInstance(null, REQUEST, TRUST);
		}

		if (!Strings.isNullOrEmpty(trust.getProjectId())) {
			requireRole(trust);
		}
		requireUserIsTrustor(context, trust);
		requireTrusteeExists(trust.getTrustee().getId());
		List<Role> allRoles = this.getAssignmentApi().listRoles(null);
		List<Role> cleanRoles = cleanRoleList(trust, allRoles);
		requireTrustorHasRoleInProject(trust, cleanRoles);
		String trustid = UUID.randomUUID().toString();
		Trust newTrust = this.getTrustApi().createTrust(trustid, trust, cleanRoles);
		fillInRoles(request, newTrust, allRoles);

		// } catch (Exception e) {
		// throw new ValidationException(null, TRUST, e.getMessage());
		// }

		return wrapMember(request, newTrust);
	}

	private void requireTrustorHasRoleInProject(Trust trust, List<Role> cleanRoles) {
		List<Role> userRoles = getUserRole(trust);

		for (Role trustRole : cleanRoles) {
			List<Role> matchingRoles = Lists.newArrayList();

			for (Role role : userRoles) {
				if (role.getId().equals(trustRole.getId())) {
					matchingRoles.add(role);
				}
			}

			if (matchingRoles.isEmpty()) {
				throw Exceptions.RoleNotFoundException.getInstance(null, trustRole.getId());
			}
		}
	}

	private List<Role> getUserRole(Trust trust) {
		if (!Strings.isNullOrEmpty(trust.getProjectId())) {
			return assignmentApi.getRolesForUserAndProject(trust.getTrustorUserId(), trust.getProjectId());
		}
		return new ArrayList<Role>();
	}

	private void requireTrusteeExists(String trusteeUserId) {
		identityApi.getUser(trusteeUserId);
	}

	private void requireUserIsTrustor(KeystoneContext context, Trust trust) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		String userid = getUserId(context);
		if (!userid.equals(trust.getTrustorUserId())) {
			throw Exceptions.ForbiddenException.getInstance("The authenticated user should match the trustor.");
		}

	}

	private void requireRole(Trust trust) {
		if (trust.getTrustRoles() == null || trust.getTrustRoles().isEmpty()) {
			throw Exceptions.ForbiddenException.getInstance("At least one role should be specified.");
		}
	}

	private List<Role> cleanRoleList(Trust trust, List<Role> allRoles) {
		List<Role> trustRoles = Lists.newArrayList();
		Map<String, Role> allRoleNames = Maps.newHashMap();
		for (TrustRole trustRole : trust.getTrustRoles()) {
			allRoleNames.put(trustRole.getRole().getName(), trustRole.getRole());
		}
		for (TrustRole trustRole : trust.getTrustRoles()) {
			if (trustRole.getRole() != null && !Strings.isNullOrEmpty(trustRole.getRole().getId())) {
				trustRoles.add(trustRole.getRole());
			} else if (trustRole.getRole() != null && !Strings.isNullOrEmpty(trustRole.getRole().getName())) {
				String roleName = trustRole.getRole().getName();
				if (allRoleNames.containsKey(roleName)) {
					trustRoles.add(allRoleNames.get(roleName));
				} else {
					String msg = String.format(ROLE_NOT_FOUND, roleName);
					throw Exceptions.RoleNotFoundException.getInstance(msg);
				}
			} else {
				throw Exceptions.ValidationException.getInstance(null, ROLES, ID_OR_NAME);
			}
		}
		return trustRoles;
	}

	@Override
	public String getName() {
		return "create_trust";
	}
}
