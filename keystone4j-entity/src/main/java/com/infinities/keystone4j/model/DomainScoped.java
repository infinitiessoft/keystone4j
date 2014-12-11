package com.infinities.keystone4j.model;

import com.infinities.keystone4j.model.assignment.Domain;

public interface DomainScoped {

	Domain getDomain();

	void setDomain(Domain domain);

}
