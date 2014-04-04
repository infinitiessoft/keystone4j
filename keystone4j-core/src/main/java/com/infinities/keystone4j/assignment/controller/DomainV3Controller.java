package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.DomainWrapper;
import com.infinities.keystone4j.model.assignment.DomainsWrapper;

public interface DomainV3Controller {

	DomainWrapper createDomain(Domain domain);

	DomainsWrapper listDomains(String name, Boolean enabled, int page, int perPage);

	DomainWrapper getDomain(String domainid);

	DomainWrapper updateDomain(String domainid, Domain domain);

	// void deleteDomainContents();

	void deleteDomain(String domainid);

}
