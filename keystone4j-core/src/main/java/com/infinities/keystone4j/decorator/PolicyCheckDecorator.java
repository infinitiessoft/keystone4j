package com.infinities.keystone4j.decorator;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.token.model.Token;

//take care of callback of grant 
public class PolicyCheckDecorator<T> extends AbstractActionDecorator<T> {

	private HttpServletRequest request;
	private final Callback callback;
	private final static Logger logger = LoggerFactory.getLogger(PolicyCheckDecorator.class);


	public PolicyCheckDecorator(Action<T> command, Callback callback) {
		super(command);
		this.callback = callback;
	}

	@Override
	public T execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else if (callback != null) {
			callback.execute(context);
		} else {
			String action = MessageFormat.format("identity:{0}", command.getClass().getName());

			Token token = buildPolicyCheckCredentials(action, context);

		}

		return command.execute();
	}

	private Token buildPolicyCheckCredentials(String action, KeystoneContext context) {
		logger.debug("RBAC: AUTHORIZING {}", action);
		
		if(context.getEnvironment()!=null&&context.getEnvironment().)

	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
