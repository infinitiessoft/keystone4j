package com.infinities.keystone4j.token.provider.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenProviderDriverFactory implements Factory<TokenProviderDriver> {

	private final Logger logger = LoggerFactory.getLogger(TokenProviderDriverFactory.class);
	private final static String PKI_PROVIDER = "com.infinities.keystone4j.token.provider.driver.PkiProvider";
	private final static String UUID_PROVIDER = "com.infinities.keystone4j.token.provider.driver.UuidProvider";
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final TrustApi trustApi;


	@Inject
	public TokenProviderDriverFactory(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi,
			TokenApi tokenApi, TrustApi trustApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.trustApi = trustApi;
	}

	@Override
	public void dispose(TokenProviderDriver arg0) {

	}

	@Override
	public TokenProviderDriver provide() {
		String provider = Config.Instance.getOpt(Config.Type.token, "provider").asText();
		String tokenFormat = Config.Instance.getOpt(Config.Type.signing, "token_format").asText();
		if (Strings.isNullOrEmpty(provider)) {
			if (Strings.isNullOrEmpty(tokenFormat)) {
				try {
					Class<?> c = Class.forName(PKI_PROVIDER);
					return (TokenProviderDriver) setUpInstance(c);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			String msg = "keystone.conf [signing] token_format is depreciated in favor of keystone.conf [token] provider";
			if (tokenFormat.equals("PKI")) {
				logger.warn(msg);
				try {
					Class<?> c = Class.forName(PKI_PROVIDER);
					return (TokenProviderDriver) setUpInstance(c);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else if (tokenFormat.equals("UUID")) {
				logger.warn(msg);
				try {
					Class<?> c = Class.forName(UUID_PROVIDER);
					return (TokenProviderDriver) setUpInstance(c);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				throw Exceptions.UnexpectedException
						.getInstance("Unrecognized keystone.conf [signing] token_format: expected either UUID or PKI");
			}

		} else {

			if (!Strings.isNullOrEmpty(tokenFormat)
					&& ((provider.equals(PKI_PROVIDER) && !tokenFormat.equals("PKI")) || (provider.equals(UUID_PROVIDER) && !tokenFormat
							.equals("UUID")))) {
				throw Exceptions.UnexpectedException
						.getInstance("keystone.conf [signing] token_format (depreciated) conflicts with keystone.conf [token] provider");
			}
			try {
				Class<?> c = Class.forName(provider);
				return (TokenProviderDriver) setUpInstance(c);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	private Object setUpInstance(Class<?> c) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?>[] oParam = new Class[4];
		oParam[0] = IdentityApi.class;
		oParam[1] = AssignmentApi.class;
		oParam[2] = CatalogApi.class;
		oParam[3] = TokenApi.class;
		oParam[4] = TrustApi.class;

		Constructor<?> constructor = c.getConstructor(oParam);
		Object[] paramObjs = new Object[4];
		paramObjs[0] = identityApi;
		paramObjs[1] = assignmentApi;
		paramObjs[2] = catalogApi;
		paramObjs[3] = tokenApi;
		paramObjs[4] = trustApi;
		return constructor.newInstance(paramObjs);
	}
}
