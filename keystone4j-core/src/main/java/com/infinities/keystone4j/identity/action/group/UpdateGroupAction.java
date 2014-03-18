package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateGroupAction extends AbstractGroupAction<Group> {

	private String groupid;
	private Group group;


	public UpdateGroupAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String groupid,
			Group group) {
		super(assignmentApi, tokenApi, identityApi);
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(groupid, group);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		requireMatchingDomainId(groupid, group);
		return this.getIdentityApi().updateGroup(groupid, group, domain.getId());
	}

	private void requireMatchingDomainId(String groupid, Group group) {
		boolean immutable = Config.Instance.getOpt(Config.Type.DEFAULT, "domain_id_immutable").asBoolean();
		if (immutable && !Strings.isNullOrEmpty(group.getDomainid())) {
			Group existedRef = identityApi.getGroup(groupid, null);
			if (!existedRef.getDomainid().equals(group.getDomainid())) {
				throw Exceptions.ValidationException.getInstance("Cannot change Domain ID");
			}
		}
	}

	@Override
	public String getName() {
		return "update_group";
	}
}
