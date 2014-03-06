package com.infinities.keystone4j.policy.model;

import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.identity.model.User;

public interface PolicyEntity {

	String getId();

	User getUser();

	Domain getDomain();

	Project getProject();
}
