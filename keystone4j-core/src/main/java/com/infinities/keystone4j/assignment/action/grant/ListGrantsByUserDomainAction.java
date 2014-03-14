package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByUserDomainAction extends AbstractGrantAction<List<Role>> {

	private final String userid;
	private final String domainid;
	private final boolean inherited;


	public ListGrantsByUserDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String userid,
			String domainid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.userid = userid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute(ContainerRequestContext request) {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireDomain(domainid);
		return assignmentApi.listGrantsByUserDomain(userid, domainid, inherited);
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
