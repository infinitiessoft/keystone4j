package com.infinities.keystone4j.decorator;

import com.infinities.keystone4j.Action;

public abstract class AbstractActionDecorator<T> implements Action<T> {

	protected Action<T> command;


	public AbstractActionDecorator(Action<T> command) {
		this.command = command;
	}

	public Action<T> getAction() {
		return command;
	}

	public void setAction(Action<T> command) {
		this.command = command;
	}

}
