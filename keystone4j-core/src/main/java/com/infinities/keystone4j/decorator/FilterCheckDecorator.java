package com.infinities.keystone4j.decorator;

import com.infinities.keystone4j.Action;

public class FilterCheckDecorator<T> extends AbstractActionDecorator<T> {

	public FilterCheckDecorator(Action<T> command) {
		super(command);
	}

	@Override
	public T execute() {
		return null;
	}

	@Override
	public String getName() {
		return "filter_check";
	}
}
