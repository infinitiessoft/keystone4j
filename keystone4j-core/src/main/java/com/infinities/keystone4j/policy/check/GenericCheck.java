package com.infinities.keystone4j.policy.check;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.Enforcer;

public class GenericCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(GenericCheck.class);


	@Override
	public String getRule() {
		return "none";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {
		try {
			String match = this.getMatch();
			String targetid = null;
			Pattern pattern = Pattern.compile("%\\((.+?)\\)s");
			Matcher matcher = pattern.matcher(match);
			if (matcher.matches()) {
				match = matcher.group(1);
			}

			if (match.startsWith("target")) {
				String replaced = match.replace("_", ".");
				String split[] = replaced.split(".");
				if (split.length < 3) {
					logger.warn("unreadable policy");
					return false;
				}
				targetid = getPolicyEntity(target.get(split[1]), Arrays.copyOfRange(split, 2, split.length - 1)).getId();
			} else {
				if (!match.contains(".")) {
					if (match.endsWith("_id")) {
						targetid = (String) parMap.get(match.replace("_", ""));
					}
				} else {
					String replaced = match.replace("_", ".");
					String split[] = replaced.split(".");
					if (split.length < 2) {
						logger.warn("unreadable policy");
						return false;
					}
					PolicyEntity entity = getPolicyEntity(target.get(split[0]),
							Arrays.copyOfRange(split, 1, split.length - 1));
					targetid = entity.getId();
				}
			}

			if (Strings.isNullOrEmpty(targetid)) {
				logger.warn("unreadable policy");
				return false;
			}
			String split[] = this.getKind().replace("_", ".").split(".");
			String leftval = getPolicyEntity(token, split).getId();

			return targetid.equals(leftval);
		} catch (Exception e) {
			logger.warn("check failed", e);
			return false;
		}
	}

	private PolicyEntity getPolicyEntity(PolicyEntity entity, String[] split) {
		if (split.length < 2) {
			throw new IllegalArgumentException("wrong match.");
		}
		if (entity == null) {
			throw new IllegalArgumentException("entity cannot be null.");
		}
		if (split[1].equals("domain")) {
			return getPolicyEntity(entity.getDomain(), Arrays.copyOfRange(split, 1, split.length - 1));
		} else if (split[1].equals("user")) {
			return getPolicyEntity(entity.getUser(), Arrays.copyOfRange(split, 1, split.length - 1));
		} else if (split[1].equals("project")) {
			return getPolicyEntity(entity.getProject(), Arrays.copyOfRange(split, 1, split.length - 1));
		} else if (split[1].equals("id")) {
			return entity;
		}

		throw new IllegalArgumentException("unknown policy.");

	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new GenericCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
