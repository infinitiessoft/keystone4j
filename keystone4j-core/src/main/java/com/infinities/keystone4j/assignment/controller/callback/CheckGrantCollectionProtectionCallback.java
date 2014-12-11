package com.infinities.keystone4j.assignment.controller.callback;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.CollectionCallback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers._check_grant_protection 20141209

public class CheckGrantCollectionProtectionCallback extends ControllerAction implements CollectionCallback {

	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final String roleid;
	private final String userid;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final boolean allowNoUser;


	public CheckGrantCollectionProtectionCallback(TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			IdentityApi identityApi, AssignmentApi assignmentApi, String userid, String roleid, String groupid,
			String projectid, String domainid) {
		this(tokenProviderApi, policyApi, identityApi, assignmentApi, userid, roleid, groupid, projectid, domainid, false);
	}

	public CheckGrantCollectionProtectionCallback(TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			IdentityApi identityApi, AssignmentApi assignmentApi, String userid, String roleid, String groupid,
			String projectid, String domainid, boolean allowNoUser) {
		super(tokenProviderApi, policyApi);
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.groupid = groupid;
		this.userid = userid;
		this.roleid = roleid;
		this.projectid = projectid;
		this.domainid = domainid;
		this.allowNoUser = allowNoUser;
	}

	@Override
	public void execute(ContainerRequestContext request, FilterProtectedAction<?> command) {
		Map<String, PolicyEntity> target = Maps.newHashMap();
		if (!Strings.isNullOrEmpty(roleid)) {
			Role role = this.assignmentApi.getRole(roleid);
			target.put("role", role);
		}
		if (!Strings.isNullOrEmpty(userid)) {
			try {
				User user = this.identityApi.getUser(userid);
				target.put("user", user);
			} catch (WebApplicationException e) {
				if (!allowNoUser) {
					throw e;
				}
			}
		} else {
			Group group = this.identityApi.getGroup(groupid, null);
			target.put("group", group);
		}

		if (!Strings.isNullOrEmpty(domainid)) {
			Domain domain = this.assignmentApi.getDomain(domainid);
			target.put("domain", domain);
		} else {
			Project project = this.assignmentApi.getProject(projectid);
			target.put("project", project);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, request, command, target);
	}
}
