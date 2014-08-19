package com.infinities.keystone4j.option;

public class StringOption extends Option {

	private final boolean secret;


	public StringOption(String name, boolean secret, String value) {
		super(name, value);
		this.secret = secret;
	}

	public boolean isSecret() {
		return secret;
	}

	@Override
	public void resetValue(String value) {
		setValue(value);
	}
}
