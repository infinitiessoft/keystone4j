package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByGroupDomainAction extends AbstractGrantAction<List<Role>> {

	private final String groupid;
	private final String domainid;
	private final boolean inherited;


	public ListGrantsByGroupDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String groupid,
			String domainid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute(ContainerRequestContext request) {
		KeystonePreconditions.requireGroup(groupid);
		KeystonePreconditions.requireDomain(domainid);
		return assignmentApi.listGrantsByGroupDomain(groupid, domainid, inherited);
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
