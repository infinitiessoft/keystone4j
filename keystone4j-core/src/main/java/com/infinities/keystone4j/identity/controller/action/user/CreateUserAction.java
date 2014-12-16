package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateUserAction extends AbstractUserAction implements ProtectedAction<User> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(CreateUserAction.class);
	private final User user;


	public CreateUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, User user) {
		super(identityApi, tokenProviderApi, policyApi);
		this.user = user;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) {
		// require_attribute(user, 'name')
		if (Strings.isNullOrEmpty(user.getName())) {
			String msg = String.format("%s field is required and cannot be empty", "name");
			throw Exceptions.ValidationException.getInstance(msg);
		}
		// ignore normalize_dict(user)
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		normalizeDomainid(context, user);
		User ret = identityApi.createUser(user);
		return wrapMember(request, ret);
	}

	@Override
	public String getName() {
		return "create_user";
	}
}
