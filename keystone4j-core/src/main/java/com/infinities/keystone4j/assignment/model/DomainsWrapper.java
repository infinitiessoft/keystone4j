package com.infinities.keystone4j.assignment.model;

import java.util.List;

public class DomainsWrapper {

	private List<Domain> domains;


	public DomainsWrapper(List<Domain> domains) {
		super();
		this.domains = domains;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

}
