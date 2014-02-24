package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByUserDomainAction extends AbstractGrantAction<List<Role>> {

	private String userid;
	private String domainid;
	private boolean inherited;


	public ListGrantsByUserDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String userid,
			String domainid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.userid = userid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireDomain(domainid);
		return assignmentApi.listGrantsByUserDomain(userid, domainid, inherited);
	}
}
