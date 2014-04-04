package com.infinities.keystone4j.decorator;

import java.text.MessageFormat;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

//take care of callback of grant 
public class PolicyCheckDecorator<T> extends AbstractActionDecorator<T> {

	private final Callback callback;
	private final static Logger logger = LoggerFactory.getLogger(PolicyCheckDecorator.class);
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public PolicyCheckDecorator(Action<T> command, Callback callback, TokenApi tokenApi, PolicyApi policyApi,
			Map<String, Object> parMap) {
		super(command, tokenApi, policyApi);
		this.callback = callback;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		this.parMap = parMap;
	}

	@Override
	public T execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Token token = (Token) request.getProperty(Authorization.AUTH_CONTEXT_ENV);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else if (callback != null) {
			callback.execute(request, command, parMap);
		} else {
			String action = MessageFormat.format("identity:{0}", command.getName());

			Token policyToken = buildPolicyCheckCredentials(action, context, token);
			Map<String, PolicyEntity> target = Maps.newHashMap();
			if (!Strings.isNullOrEmpty(context.getSubjectTokenid())) {
				Token subjectToken = tokenApi.getToken(context.getSubjectTokenid());
				target.put("user", subjectToken.getUser());
				target.put("domain", subjectToken.getDomain());
				target.put("project", subjectToken.getProject());
			}
			policyApi.enforce(policyToken, action, target, parMap, true);

		}

		return command.execute(request);
	}

	@Override
	public String getName() {
		return "policy_check";
	}

}
