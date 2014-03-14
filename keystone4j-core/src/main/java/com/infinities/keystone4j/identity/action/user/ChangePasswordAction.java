package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;

public class ChangePasswordAction extends AbstractUserAction<User> {

	private final static String ORIGINAL_PASSWORD = "original_password";
	private final static String USER = "user";
	private final static String PASSWORD = "password";
	private String userid;
	private UserParam user;


	public ChangePasswordAction(IdentityApi identityApi, String userid, UserParam user) {
		super(identityApi);
	}

	@Override
	public User execute(ContainerRequestContext request) {
		String originalPassword = user.getOriginalPassword();
		if (Strings.isNullOrEmpty(originalPassword)) {
			throw Exceptions.ValidationException.getInstance(null, ORIGINAL_PASSWORD, USER);
		}
		String password = user.getPassword();
		if (Strings.isNullOrEmpty(password)) {
			throw Exceptions.ValidationException.getInstance(null, PASSWORD, USER);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);

		try {
			this.getIdentityApi().authenticate(userid, password, domain.getId());
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance();
		}

		KeystonePreconditions.requireMatchingId(userid, user);

		return this.getIdentityApi().updateUser(userid, user, domain.getId());
	}

	@Override
	public String getName() {
		return "change_password";
	}
}
