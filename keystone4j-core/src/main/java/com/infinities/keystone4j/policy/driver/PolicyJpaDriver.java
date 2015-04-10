package com.infinities.keystone4j.policy.driver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.decorator.HandleConflictsDecorator;
import com.infinities.keystone4j.jpa.impl.PolicyDao;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.EnforcerImpl;
import com.infinities.keystone4j.policy.PolicyDriver;

public class PolicyJpaDriver implements PolicyDriver {

	private final static Logger logger = LoggerFactory.getLogger(PolicyJpaDriver.class);
	private final static String ENFORCE_ACTION_CREDENTIALS = "enforce {}: {}";
	private final static String POLICY_FILE = "policy_file";
	private final PolicyDao policyDao;
	private static String POLICY_PATH = "";
	private static EnforcerImpl enforcer;


	public PolicyJpaDriver() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		super();
		this.policyDao = new PolicyDao();
	}

	@Override
	public Policy createPolicy(String policyid, Policy policy) {
		HandleConflictsDecorator<Policy> decorator = new HandleConflictsDecorator<Policy>(policyDao, "policy");
		decorator.persist(policy);
		return policy;
	}

	@Override
	public List<Policy> listPolicies() {
		return policyDao.findAll();
	}

	@Override
	public Policy getPolicy(String policyid) {
		Policy policy = policyDao.findById(policyid);
		if (policy == null) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
		return policy;
	}

	@Override
	public Policy updatePolicy(String policyid, Policy policy) {
		Policy oldPolicy = getPolicy(policyid);
		if (policy.isBlobUpdated()) {
			oldPolicy.setBlob(policy.getBlob());
		}
		if (policy.isDescriptionUpdated()) {
			oldPolicy.setDescription(policy.getDescription());
		}
		if (policy.isExtraUpdated()) {
			oldPolicy.setExtra(policy.getExtra());
		}
		if (policy.isTypeUpdated()) {
			oldPolicy.setType(policy.getType());
		}
		if (policy.isUserUpdated()) {
			oldPolicy.setUserId(policy.getUserId());
		}
		if (policy.isProjectUpdated()) {
			oldPolicy.setProjectId(policy.getProjectId());
		}
		return policyDao.merge(oldPolicy);
	}

	@Override
	public void deletePolicy(String policyid) {
		Policy policy = getPolicy(policyid);
		policyDao.remove(policy);
	}

	@Override
	public Policy enforce(Context credentials, String action, Map<String, Object> target) throws Exception {
		logger.debug(ENFORCE_ACTION_CREDENTIALS, new Object[] { action, credentials });
		enforce(credentials, action, target, true);
		return null;
	}

	public boolean enforce(Context credentials, String action, Map<String, Object> target, boolean doRaise) throws Exception {
		init();

		Exception e = null;

		if (doRaise) {
			e = Exceptions.ForbiddenException.getInstance();
		}
		return enforcer.enforce(action, target, credentials, doRaise, e);
	}

	private void init() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		synchronized (POLICY_PATH) {
			if (Strings.isNullOrEmpty(POLICY_PATH)) {
				POLICY_PATH = Config.Instance.getOpt(Config.Type.DEFAULT, POLICY_FILE).asText();
			}
			if (enforcer == null) {
				enforcer = new EnforcerImpl(POLICY_PATH, null, null, true);
			}
		}
	}

	@Override
	public Integer getListLimit() {
		int limit = -1;
		try {
			limit = Config.Instance.getOpt(Config.Type.policy, "list_limit").asInteger();
		} catch (Exception e) {

		}
		if (limit != -1) {
			return limit;
		}
		try {
			limit = Config.Instance.getOpt(Config.Type.DEFAULT, "list_limit").asInteger();
		} catch (Exception e) {

		}
		return limit;
	}
}
