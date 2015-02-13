package com.infinities.keystone4j.model.token;

import java.util.Calendar;

import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;

public interface IToken {

	User getUser();

	Metadata getMetadata();

	Calendar getExpires();

	ITokenDataWrapper getTokenData();

	String getId();

	Bind getBind();

	Project getTenant();

	String getParentAuditId();

}
