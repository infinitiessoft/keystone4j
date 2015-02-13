package com.infinities.keystone4j.policy.check;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class HttpCheck extends Check {

	private final Logger logger = LoggerFactory.getLogger(HttpCheck.class);


	@Override
	public String getRule() {
		return "http";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		logger.warn("HttpCheck not implemented yet");
		return false;
		// String url = "http:" + MessageFormat.format(getMatch(),
		// target.getUrl());

	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new HttpCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
