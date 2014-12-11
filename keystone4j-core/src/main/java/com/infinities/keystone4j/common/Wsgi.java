package com.infinities.keystone4j.common;

import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.token.model.KeystoneToken;

//keystone.common.wsgi 20141128
public class Wsgi {

	private final static Logger logger = LoggerFactory.getLogger(Wsgi.class);


	private Wsgi() {

	}

	public static void validateTokenBind(KeystoneContext context, KeystoneToken token) {
		String bindMode = Config.Instance.getOpt(Config.Type.token, "enforce_token_bind").asText();

		if (bindMode.equals("disabled")) {
			return;
		}

		Bind bind = token.getBind();
		boolean permissive = bindMode.equals("permissive") || bindMode.equals("strict");

		String name = permissive || bindMode.equals("required") ? null : bindMode;

		if (bind == null) {
			if (permissive) {
				return;
			} else {
				logger.info("no bind information present in token");
				throw Exceptions.UnauthorizedException.getInstance();
			}
		}

		if (!Strings.isNullOrEmpty(name) && !bind.containsKey(name)) {
			logger.info("Named bind mode {} not in bind information", name);
			throw Exceptions.UnauthorizedException.getInstance();
		}

		for (Entry<String, String> entry : bind.entrySet()) {
			String bindType = entry.getKey();
			String identifier = entry.getValue();

			if ("kerberos".equals(bindType)) {
				if (!"negotiate".equals(context.getEnvironment().getAuthType().toLowerCase())) {
					logger.info("Kerberos credentials required and not present");
					throw Exceptions.UnauthorizedException.getInstance();
				}

				if (!identifier.equals(context.getEnvironment().getRemoteUser())) {
					logger.info("Kerberos credentials do not match those in bind");
					throw Exceptions.UnauthorizedException.getInstance();
				}
			} else if ("permissive".equals(bindType)) {
				logger.info("Ignoring unknown bind for permissive mode: {}: {}", new Object[] { bindType, identifier });
			} else {
				logger.info("Couldn't verify unknown bind: {}: {}", new Object[] { bindType, identifier });
				throw Exceptions.UnauthorizedException.getInstance();
			}
		}

	}

	public static String getBaseUrl(ContainerRequestContext context, String endpointType) {
		String url = Config.Instance.getOpt(Config.Type.DEFAULT, String.format("%s_endpoint", endpointType)).asText();

		if (Strings.isNullOrEmpty(url)) {
			url = context.getUriInfo().getBaseUri().toString();
		} else {
			url = Config.replaceVarWithConf(url);
		}

		return StringUtils.removeEnd(url, "/");
	}
}
