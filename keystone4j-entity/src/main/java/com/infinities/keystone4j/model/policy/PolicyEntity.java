package com.infinities.keystone4j.model.policy;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;

public interface PolicyEntity {

	String getId();

	User getUser();

	Domain getDomain();

	Project getProject();
}
