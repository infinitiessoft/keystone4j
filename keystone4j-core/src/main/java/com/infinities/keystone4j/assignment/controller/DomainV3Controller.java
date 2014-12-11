package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;

public interface DomainV3Controller {

	public final static String collectionName = "domains";
	public final static String memberName = "domain";


	MemberWrapper<Domain> createDomain(Domain domain) throws Exception;

	CollectionWrapper<Domain> listDomains() throws Exception;

	MemberWrapper<Domain> getDomain(String domainid) throws Exception;

	MemberWrapper<Domain> updateDomain(String domainid, Domain domain) throws Exception;

	// void deleteDomainContents();

	void deleteDomain(String domainid) throws Exception;

}
