package com.infinities.keystone4j.assignment.callback;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.PolicyCredentialChecker;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

public class CheckGrantCallback extends PolicyCredentialChecker implements Callback {

	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final String roleid;
	private final String userid;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;


	public CheckGrantCallback(TokenApi tokenApi, PolicyApi policyApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			String userid, String roleid, String groupid, String projectid, String domainid) {
		super(tokenApi, policyApi);
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.groupid = groupid;
		this.userid = userid;
		this.roleid = roleid;
		this.projectid = projectid;
		this.domainid = domainid;
	}

	@Override
	public void execute(ContainerRequestContext request, Action<?> command, Map<String, Object> parMap) {
		Map<String, PolicyEntity> target = Maps.newHashMap();
		if (!Strings.isNullOrEmpty(domainid)) {
			Domain domain = this.assignmentApi.getDomain(domainid);
			target.put("domain", domain);
		}
		if (!Strings.isNullOrEmpty(groupid)) {
			Group group = this.identityApi.getGroup(groupid, null);
			target.put("group", group);
		}
		if (!Strings.isNullOrEmpty(userid)) {
			User user = this.identityApi.getUser(userid, null);
			target.put("user", user);
		}
		if (!Strings.isNullOrEmpty(projectid)) {
			Project project = this.assignmentApi.getProject(projectid);
			target.put("project", project);
		}
		if (!Strings.isNullOrEmpty(roleid)) {
			Role role = this.assignmentApi.getRole(roleid);
			target.put("role", role);
		}

		Token token = (Token) request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, token, command, target, parMap);
	}

}
