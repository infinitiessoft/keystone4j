package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class CheckGrantByUserDomainAction extends AbstractGrantAction<Role> {

	private String roleid;
	private String userid;
	private String domainid;
	private boolean inherited;


	public CheckGrantByUserDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid, String userid,
			String domainid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.roleid = roleid;
		this.userid = userid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public Role execute() {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireDomain(domainid);

		// TODO try to test if user exist?
		this.getIdentityApi().getUser(userid, null);

		assignmentApi.getGrantByUserDomain(roleid, userid, domainid, inherited);

		return null;
	}
}
