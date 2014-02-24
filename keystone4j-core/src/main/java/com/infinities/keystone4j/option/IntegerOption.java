package com.infinities.keystone4j.option;

public class IntegerOption extends Option {

	private int intValue;


	public IntegerOption(String name, String value) {
		super(name, value);
		intValue = Integer.parseInt(value);
	}

	public IntegerOption(String name, int intValue) {
		super(name, String.valueOf(intValue));
		this.intValue = intValue;
	}

	@Override
	public int getIntValue() {
		return intValue;
	}

	@Override
	public void resetValue(String value) {
		setValue(value);
		intValue = Integer.parseInt(value);
	}

}
