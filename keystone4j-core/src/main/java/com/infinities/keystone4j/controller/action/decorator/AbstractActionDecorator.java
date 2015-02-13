package com.infinities.keystone4j.controller.action.decorator;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

@Deprecated
public abstract class AbstractActionDecorator<T> extends ControllerAction implements ProtectedAction<T> {

	protected ProtectedAction<T> command;


	public AbstractActionDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.command = command;
	}

	public ProtectedAction<T> getAction() {
		return command;
	}

	public void setAction(ProtectedAction<T> command) {
		this.command = command;
	}

}
