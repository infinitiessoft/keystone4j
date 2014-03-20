package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;
import com.infinities.keystone4j.token.TokenApi;

public class ChangePasswordAction extends AbstractUserAction<User> {

	private final static String ORIGINAL_PASSWORD = "original_password";
	private final static String USER = "user";
	private final static String PASSWORD = "password";
	private final String userid;
	private final UserParam param;


	// private final Logger logger =
	// LoggerFactory.getLogger(ChangePasswordAction.class);

	public ChangePasswordAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String userid,
			UserParam param) {
		super(assignmentApi, identityApi, tokenApi);
		this.param = param;
		this.userid = userid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		String originalPassword = param.getOriginalPassword();
		if (Strings.isNullOrEmpty(originalPassword)) {
			throw Exceptions.ValidationException.getInstance(null, ORIGINAL_PASSWORD, USER);
		}
		String password = param.getPassword();
		if (Strings.isNullOrEmpty(password)) {
			throw Exceptions.ValidationException.getInstance(null, PASSWORD, USER);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);

		try {
			this.getIdentityApi().authenticate(userid, originalPassword, domain.getId());
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}
		User user = identityApi.getUser(userid, domain.getId());
		user.setPassword(password);

		// KeystonePreconditions.requireMatchingId(userid, user);

		return this.getIdentityApi().updateUser(userid, user, domain.getId());
	}

	@Override
	public String getName() {
		return "change_password";
	}
}
