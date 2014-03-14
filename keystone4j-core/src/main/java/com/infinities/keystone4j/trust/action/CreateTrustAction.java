package com.infinities.keystone4j.trust.action;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustRole;

public class CreateTrustAction extends AbstractTrustAction<Trust> {

	private final static String TRUST = "trust";
	private final static String REQUEST = "request";
	private final static String ROLES = "roles";
	private final static String ID_OR_NAME = "id or name";
	private final static String ROLE_NOT_FOUND = "role {0} is not defined";
	private final Trust trust;


	public CreateTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			Trust trust) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trust = trust;
	}

	@Override
	public Trust execute(ContainerRequestContext request) {
		if (trust == null) {
			throw Exceptions.ValidationException.getInstance(null, REQUEST, TRUST);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		// try {
		User user = new KeystoneUtils().getUser(context);
		trustorOnly(trust, user);

		User trustee = this.getIdentityApi().getUser(trust.getTrustee().getId(), null);
		if (trustee == null) {
			throw Exceptions.UserNotFoundException.getInstance(null, trust.getTrustee().getId());
		}

		List<Role> allRoles = this.getAssignmentApi().listRoles();

		// roles in trust
		List<Role> cleanRoles = cleanRoleList(trust, allRoles);

		// roles user owned
		List<Role> userRoles = Lists.newArrayList();

		if (trust.getProject() != null && Strings.isNullOrEmpty(trust.getProject().getId())) {
			userRoles.addAll(this.getAssignmentApi().getRolesForUserAndProject(user.getId(), trust.getProject().getId()));
		}

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

		// TODO handle expireAt date format issue

		Trust newTrust = this.getTrustApi().createTrust(trust, cleanRoles);

		// openstack separate trust database and assignment database so they
		// need to check all roles in trust database is also in assignment
		// database. however, we use the same database so it can be ignore.
		TrustUtils.fillInRoles(newTrust, allRoles);

		// } catch (Exception e) {
		// throw new ValidationException(null, TRUST, e.getMessage());
		// }

		return newTrust;
	}

	private void trustorOnly(Trust trust, User user) {
		if (user.getId() != trust.getTrustor().getId()) {
			throw Exceptions.ForbiddenException.getInstance();

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
					String msg = MessageFormat.format(ROLE_NOT_FOUND, roleName);
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
