package com.infinities.keystone4j.decorator;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

public class FilterCheckDecorator<T> extends AbstractActionDecorator<T> {

	private HttpServletRequest request;
	private final static Logger logger = LoggerFactory.getLogger(FilterCheckDecorator.class);
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public FilterCheckDecorator(Action<T> command, TokenApi tokenApi, PolicyApi policyApi, Map<String, Object> parMap) {
		super(command, tokenApi, policyApi);
		this.parMap = parMap;
		this.policyApi = policyApi;
	}

	@Override
	public T execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = MessageFormat.format("identity:{0}", command.getName());

			Token token = buildPolicyCheckCredentials(action, context);
			Map<String, PolicyEntity> target = Maps.newHashMap();
			policyApi.enforce(token, action, target, parMap, true);
		}

		return command.execute();
	}

	@Override
	public String getName() {
		return "filter_check";
	}
}
