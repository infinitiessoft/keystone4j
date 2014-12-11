package com.infinities.keystone4j.model.trust;

import com.infinities.keystone4j.model.MemberWrapper;

public class SignedWrapper implements MemberWrapper<String> {

	private String signed;


	public SignedWrapper() {

	}

	public SignedWrapper(String signed) {
		super();
		this.signed = signed;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	@Override
	public void setRef(String ref) {
		this.signed = ref;
	}

}
