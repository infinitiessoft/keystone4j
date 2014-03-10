package com.infinities.keystone4j.decorator;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.PolicyCredentialChecker;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractActionDecorator<T> extends PolicyCredentialChecker implements Action<T> {

	protected Action<T> command;


	public AbstractActionDecorator(Action<T> command, TokenApi tokenApi, PolicyApi policyApi) {
		super(tokenApi, policyApi);
		this.command = command;
	}

	public Action<T> getAction() {
		return command;
	}

	public void setAction(Action<T> command) {
		this.command = command;
	}

}
