package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class CheckGrantByGroupDomainAction extends AbstractGrantAction<Role> {

	private final String roleid;
	private final String groupid;
	private final String domainid;
	private final boolean inherited;


	public CheckGrantByGroupDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
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

		// TODO try to test if group exist?
		this.getIdentityApi().getGroup(groupid, null);

		assignmentApi.getGrantByGroupDomain(roleid, groupid, domainid, inherited);

		return null;
	}

	@Override
	public String getName() {
		return "check_grant";
	}
}
