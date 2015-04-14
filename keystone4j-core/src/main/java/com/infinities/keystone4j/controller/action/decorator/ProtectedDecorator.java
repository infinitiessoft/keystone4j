package com.infinities.keystone4j.controller.action.decorator;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.JsonUtils;

//keystone.common.controller.protected 20141203
//take care of callback of grant 
public class ProtectedDecorator<T> extends ControllerAction implements ProtectedAction<T> {

	private final Callback callback;
	private final static Logger logger = LoggerFactory.getLogger(ProtectedDecorator.class);
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;
	private final T ref;
	private final Object args;
	private final ProtectedAction<T> command;


	// public ProtectedDecorator(ProtectedAction<T> command, TokenProviderApi
	// tokenProviderApi, PolicyApi policyApi, T args) {
	// this(command, null, tokenProviderApi, policyApi, null, args);
	// }

	public ProtectedDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this(command, null, tokenProviderApi, policyApi, null, null);
	}

	public ProtectedDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi, T ref,
			Object args) {
		this(command, null, tokenProviderApi, policyApi, ref, args);
	}

	public ProtectedDecorator(ProtectedAction<T> command, Callback callback, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this(command, callback, tokenProviderApi, policyApi, null, null);
	}

	public ProtectedDecorator(ProtectedAction<T> command, Callback callback, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, T ref, Object args) {
		super(tokenProviderApi, policyApi);
		this.command = command;
		this.callback = callback;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.ref = ref;
		this.args = args;
	}

	@Override
	public MemberWrapper<T> execute(ContainerRequestContext request) throws Exception {
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
				logger.debug("member name: {}, user_id: {}", new Object[] { this.getMemberName(), tokenRef.getUserId() });
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

		return command.execute(request);
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

	@Override
	public MemberWrapper<T> getMemberWrapper() {
		return command.getMemberWrapper();
	}

}
