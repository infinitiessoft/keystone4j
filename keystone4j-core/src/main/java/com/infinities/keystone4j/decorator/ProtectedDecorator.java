package com.infinities.keystone4j.decorator;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.common.controller.protected 20141203
//take care of callback of grant 
public class ProtectedDecorator<T> extends ControllerAction implements ProtectedAction<T> {

	private final Callback callback;
	private final static Logger logger = LoggerFactory.getLogger(ProtectedDecorator.class);
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;
	private final PolicyEntity member;
	private final ProtectedAction<T> command;


	public ProtectedDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this(command, null, tokenProviderApi, policyApi, null);
	}

	public ProtectedDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			PolicyEntity member) {
		this(command, null, tokenProviderApi, policyApi, member);
	}

	public ProtectedDecorator(ProtectedAction<T> command, Callback callback, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this(command, callback, tokenProviderApi, policyApi, null);
	}

	public ProtectedDecorator(ProtectedAction<T> command, Callback callback, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, PolicyEntity member) {
		super(tokenProviderApi, policyApi);
		this.command = command;
		this.callback = callback;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.member = member;
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
			Map<String, PolicyEntity> policyDictTarget = Maps.newHashMap();

			if (member != null) {
				policyDictTarget.put(this.getMemberName(), member);
			}

			if (!Strings.isNullOrEmpty(context.getSubjectTokenid())) {
				final KeystoneToken tokenRef = new KeystoneToken(context.getSubjectTokenid(),
						tokenProviderApi.validToken(context.getSubjectTokenid()));

				PolicyEntityImpl member = new PolicyEntityImpl();
				member.setUserId(tokenRef.getUserId());
				String userDomainId = null;
				User user = null;

				try {
					userDomainId = tokenRef.getUserDomainId();
				} catch (Exception e) {
					userDomainId = null;
				}

				if (!Strings.isNullOrEmpty(userDomainId)) {
					user = new User();
					Domain domain = new Domain();
					domain.setId(userDomainId);
					user.setDomain(domain);
					member.setUser(user);
				}

				policyDictTarget.put(this.getMemberName(), member);
			}
			policyApi.enforce(creds, action, policyDictTarget, true);
			logger.debug("RBAC: Authorization granted");

		}

		return command.execute(request);
	}


	public class PolicyEntityImpl implements PolicyEntity {

		private String userId;
		private User user;


		public PolicyEntityImpl() {

		}

		@Override
		public String getUserId() {
			return userId;
		}

		@Override
		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

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
