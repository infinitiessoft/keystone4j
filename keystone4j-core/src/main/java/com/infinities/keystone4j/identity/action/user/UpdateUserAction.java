package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateUserAction extends AbstractUserAction<User> {

	private final String userid;
	private final User user;


	public UpdateUserAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String userid, User user) {
		super(assignmentApi, identityApi, tokenApi);
		this.user = user;
		this.userid = userid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(userid, user);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		requireMatchingDomainId(userid, user);
		return this.getIdentityApi().updateUser(userid, user, domain.getId());
	}

	private void requireMatchingDomainId(String userid, User user) {
		boolean immutable = Config.Instance.getOpt(Config.Type.DEFAULT, "domain_id_immutable").asBoolean();
		if (immutable && !Strings.isNullOrEmpty(user.getDomainid())) {
			User existedRef = identityApi.getUser(userid, null);
			if (!existedRef.getDomainid().equals(user.getDomainid())) {
				throw Exceptions.ValidationException.getInstance("Cannot change Domain ID");
			}
		}
	}

	@Override
	public String getName() {
		return "update_user";
	}
}
