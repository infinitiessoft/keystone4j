package com.infinities.keystone4j.controller.action.decorator;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.CollectionCallback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

//keystone.common.controller.protected 20141203
//take care of callback of grant 
public class ProtectedCollectionDecorator<T> extends ControllerAction implements FilterProtectedAction<T> {

	private final CollectionCallback callback;
	private final static Logger logger = LoggerFactory.getLogger(ProtectedCollectionDecorator.class);
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;
	private final T ref;
	private final Object args;
	private final FilterProtectedAction<T> command;


	public ProtectedCollectionDecorator(FilterProtectedAction<T> command, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this(command, null, tokenProviderApi, policyApi, null, null);
	}

	public ProtectedCollectionDecorator(FilterProtectedAction<T> command, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, T member, Object args) {
		this(command, null, tokenProviderApi, policyApi, member, args);
	}

	public ProtectedCollectionDecorator(FilterProtectedAction<T> command, CollectionCallback callback,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this(command, callback, tokenProviderApi, policyApi, null, null);
	}

	public ProtectedCollectionDecorator(FilterProtectedAction<T> command, CollectionCallback callback,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, T member, Object args) {
		super(tokenProviderApi, policyApi);
		this.command = command;
		this.callback = callback;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.ref = member;
		this.args = args;
	}

	@Override
	public CollectionWrapper<T> execute(ContainerRequestContext request, String... filters) throws Exception {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else if (callback != null) {
			callback.execute(request, command);
		} else {
			String action = String.format("identity:%s", command.getName());
			AuthContext creds = buildPolicyCheckCredentials(action, context, request);
			Map<String, Object> policyDict = new HashMap<String, Object>();

			if (ref != null) {
				Map<String, Object> target = new HashMap<String, Object>();
				target.put(command.getMemberName(), ref);
				policyDict.put("target", target);
			}

			if (!Strings.isNullOrEmpty(context.getSubjectTokenid())) {
				final KeystoneToken tokenRef = new KeystoneToken(context.getSubjectTokenid(),
						tokenProviderApi.validateToken(context.getSubjectTokenid(), null));

				Map<String, Object> target = new HashMap<String, Object>();

				policyDict.put("target", target);
				Map<String, Object> memberNameMap = new HashMap<String, Object>();
				target.put(this.getMemberName(), memberNameMap);
				memberNameMap.put("user_id", tokenRef.getUserId());

				String userDomainId = null;
				try {
					userDomainId = tokenRef.getUserDomainId();
				} catch (Exception e) {
					userDomainId = null;
				}

				if (!Strings.isNullOrEmpty(userDomainId)) {
					Map<String, Object> userMap = new HashMap<String, Object>(1);
					memberNameMap.put("user", userMap);
					Map<String, Object> domainMap = new HashMap<String, Object>(1);
					userMap.put("domain", domainMap);
					domainMap.put("id", userDomainId);
				}
			}
			if (args != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> argsMap = JsonUtils.convertValue(args, Map.class);
				policyDict.putAll(argsMap);
			}
			policyApi.enforce(creds, action, policyDict);
			logger.debug("RBAC: Authorization granted");

		}

		return command.execute(request, filters);
	}

	@Override
	public String getName() {
		return command.getName();// "policy_check";
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
