package com.infinities.keystone4j.decorator;

import com.infinities.keystone4j.Action;

//take care of callback of grant 
public class PolicyCheckDecorator<T> extends AbstractActionDecorator<T> {

	public PolicyCheckDecorator(Action<T> command) {
		super(command);
	}

	@Override
	public T execute() {
		return null;
	}

}
