package com.infinities.keystone4j.trust.model;

import java.util.List;

public class TrustsWrapper {

	private List<Trust> trusts;


	public TrustsWrapper(List<Trust> trusts) {
		super();
		this.trusts = trusts;
	}

	public List<Trust> getTrusts() {
		return trusts;
	}

	public void setTrusts(List<Trust> trusts) {
		this.trusts = trusts;
	}

}
