package com.infinities.keystone4j.option;

public class BooleanOption extends Option {

	private boolean boolValue;


	public BooleanOption(String name, String value) {
		super(name, value);
		boolValue = Boolean.parseBoolean(value);
	}

	public BooleanOption(String name, boolean boolValue) {
		super(name, String.valueOf(boolValue));
		this.boolValue = boolValue;
	}

	@Override
	public boolean asBoolean() {
		return boolValue;
	}

	@Override
	public void resetValue(String value) {
		setValue(value);
		boolValue = Boolean.parseBoolean(value);
	}

	@Override
	public Option clone() {
		return new BooleanOption(this.getName(), this.boolValue);
	}

}
