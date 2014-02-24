package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class DeleteGrantByGroupDomainAction extends AbstractGrantAction<Role> {

	private String roleid;
	private String groupid;
	private String domainid;
	private boolean inherited;


	public DeleteGrantByGroupDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
			String groupid, String domainid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.roleid = roleid;
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public Role execute() {
		KeystonePreconditions.requireGroup(groupid);
		KeystonePreconditions.requireDomain(domainid);
		assignmentApi.deleteGrantByGroupDomain(roleid, groupid, domainid, inherited);
		return null;
	}
}
