package com.infinities.keystone4j.policy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.check.BaseCheck;

public class EnforcerImpl implements Enforcer {

	private final static Logger logger = LoggerFactory.getLogger(EnforcerImpl.class);
	private Rules rules;
	private String defaultRule;
	private String policyPath;
	private String policyFile;
	private boolean useConf;


	// useConf = true
	public EnforcerImpl(String policyFile, Map<String, BaseCheck> rules, String defaultRule, boolean useConf) {
		this.defaultRule = defaultRule;
		if (Strings.isNullOrEmpty(this.defaultRule)) {
			this.defaultRule = Config.Instance.getOpt(Config.Type.DEFAULT, "policy_default_rule").asText();
		}
		this.rules = new Rules(rules, defaultRule);

		this.policyPath = null;
		this.policyFile = policyFile;
		if (Strings.isNullOrEmpty(this.policyFile)) {
			this.policyFile = Config.Instance.getOpt(Config.Type.DEFAULT, "policy_file").asText();
		}
		this.useConf = useConf;
		logger.debug("defaultRule in enforcer: {}", defaultRule);
	}

	// overwrite=true, useConf=false
	@Override
	public void setRules(Map<String, BaseCheck> rules, boolean overwrite, boolean useConf) {
		this.useConf = useConf;
		if (overwrite) {
			this.rules = new Rules(rules, defaultRule);
		} else {
			this.rules.putAll(rules);
		}
	}

	@Override
	public void clear() {
		setRules(new HashMap<String, BaseCheck>(), true, false);
		this.defaultRule = null;
		this.policyPath = null;
	}

	// forceReload=false
	@Override
	public void loadRules(boolean forceReload) throws IOException {
		if (forceReload) {
			this.useConf = forceReload;
		}

		if (useConf) {
			if (Strings.isNullOrEmpty(policyPath)) {
				this.policyPath = getPolicyPath(this.policyFile);
			}

			loadPolicyFile(policyPath, forceReload, true);
			// for path in CONF.policy_dirs:
			// try:
			// path = self._get_policy_path(path)
			// except cfg.ConfigFilesNotFoundError:
			// LOG.warn(_LW("Can not find policy directories %s"), path)
			// continue
			// self._walk_through_policy_directory(path,
			// self._load_policy_file,
			// force_reload, False)
		}
	}

	// overwrite=true
	private void loadPolicyFile(String path, boolean forceReload, boolean overwrite) throws IOException {
		if (forceReload || rules.isEmpty() || !overwrite) {
			String data = Files.asCharSource(new File(policyPath), Charsets.UTF_8).read();
			Rules rules = Rules.loadJson(data, defaultRule);
			setRules(rules.getRules(), true, false);
			logger.debug("Rules successfully reloaded");
		}
	}

	private String getPolicyPath(String path) {
		String policyFile = Config.Instance.findFile(path);

		if (!Strings.isNullOrEmpty(policyFile)) {
			return policyFile;
		}
		throw Exceptions.ConfigFileNotFoundException.getInstance(path);
	}

	@Override
	public boolean enforce(Object rule, Map<String, Object> target, Context creds, boolean doRaise, Exception ex)
			throws Exception {
		loadRules(false);
		boolean result = false;
		if (rule instanceof BaseCheck) {
			result = ((BaseCheck) rule).check(target, creds, this);
		} else if (rules == null) {
			result = false;
		} else {
			try {
				result = rules.get(rule).check(target, creds, this);
			} catch (Exception e) {
				logger.debug("Rule [{}] doesn't exist", rule, e);
				result = false;
			}
		}

		if (doRaise && !result) {
			if (ex != null) {
				throw ex;
			}

			throw Exceptions.PolicyNotAuthorizedException.getInstance(null, rule);
		}
		return result;
	}

	@Override
	public Rules getRules() {
		return this.rules;
	}
}
