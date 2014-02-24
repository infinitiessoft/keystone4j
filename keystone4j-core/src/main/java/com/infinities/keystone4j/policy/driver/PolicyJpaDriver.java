package com.infinities.keystone4j.policy.driver;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.PolicyNotFoundException;
import com.infinities.keystone4j.jpa.impl.PolicyDao;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class PolicyJpaDriver implements PolicyDriver {

	private final static Logger logger = LoggerFactory.getLogger(PolicyJpaDriver.class);
	private final static String ENFORCE_ACTION_CREDENTIALS = "enforce {}: {}";
	private final static String POLICY_FILE = "policy_file";
	private final PolicyDao policyDao;
	private final Enforcer enforcer;


	public PolicyJpaDriver() throws JsonParseException, JsonMappingException, IOException {
		super();
		this.policyDao = new PolicyDao();
		String policyPath = Config.Instance.getOpt(Config.Type.DEFAULT, POLICY_FILE).getText();
		this.enforcer = new Enforcer(policyPath);
	}

	@Override
	public Policy createPolicy(Policy policy) {
		policyDao.persist(policy);
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
			throw new PolicyNotFoundException(null, policyid);
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
		return policyDao.merge(oldPolicy);
	}

	@Override
	public void deletePolicy(String policyid) {
		Policy policy = getPolicy(policyid);
		policyDao.remove(policy);
	}

	@Override
	public Policy enforce(Token token, String action, Target target, boolean doRaise) {
		logger.debug(ENFORCE_ACTION_CREDENTIALS, new Object[] { action, token });
		enforcer.enforce(token, action, target, doRaise);
		return null;
	}
}
