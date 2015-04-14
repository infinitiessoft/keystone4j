package com.infinities.keystone4j.controller.action.decorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.common.controller.filterprotected 20141204
public class FilterProtectedDecorator<T> extends ControllerAction implements FilterProtectedAction<T> {

	private final static Logger logger = LoggerFactory.getLogger(FilterProtectedDecorator.class);
	private final PolicyApi policyApi;
	private final FilterProtectedAction<T> command;
	private final Entry<String, String> entrys;


	public FilterProtectedDecorator(FilterProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this(command, tokenProviderApi, policyApi, null);
	}

	public FilterProtectedDecorator(FilterProtectedAction<T> command, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, Entry<String, String> entrys) {
		super(tokenProviderApi, policyApi);
		this.command = command;
		this.policyApi = policyApi;
		this.entrys = entrys;
	}

	@Override
	public CollectionWrapper<T> execute(ContainerRequestContext request, String... filters) throws Exception {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		// Token token = (Token)
		// request.getProperty(Authorization.AUTH_CONTEXT_ENV);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = String.format("identity:%s", command.getName());
			logger.debug("action: {}", action);
			Authorization.AuthContext creds = buildPolicyCheckCredentials(action, context, request);

			Map<String, Object> target = new HashMap<String, Object>();

			if (filters != null) {
				for (String item : filters) {
					if (request.getUriInfo().getQueryParameters().containsKey(item)) {
						target.put(item, request.getUriInfo().getQueryParameters().getFirst(item));
					}
				}
				logger.debug("RBAC: Adding query filters params ({})", target);
			}

			if (entrys != null) {
				target.put(entrys.getKey(), entrys.getValue());
			}

			policyApi.enforce(creds, action, target);

			logger.debug("RBAC: Authorization granted");
		}

		return command.execute(request, filters);
	}

	@Override
	public String getName() {
		return command.getName();// "filter_check";
	}

	@Override
	public String getCollectionName() {
		return command.getCollectionName();
	}

	@Override
	public String getMemberName() {
		return command.getMemberName();
	}

}
