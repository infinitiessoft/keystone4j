package com.infinities.keystone4j.policy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.policy.check.Check;
import com.infinities.keystone4j.policy.check.FalseCheck;
import com.infinities.keystone4j.policy.check.GenericCheck;
import com.infinities.keystone4j.policy.check.HttpCheck;
import com.infinities.keystone4j.policy.check.RoleCheck;
import com.infinities.keystone4j.policy.check.RuleCheck;
import com.infinities.keystone4j.policy.check.StringCheck;
import com.infinities.keystone4j.policy.check.TrueCheck;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class Enforcer {

	private final static Logger logger = LoggerFactory.getLogger(Enforcer.class);
	private final static String NO_HANDLER = "No handler for matches of kind {}";
	private final static String FAIL_TO_UNDERSTAND_RULE = "Failed to understand rule {}";
	private final static String RULE_ENFORCED = "Rule {} will be now enforced";
	private final static String NONE = "none";
	private final static String RULE_DOES_NOT_EXIST = "Rule [{}] doesn't exist";
	private final Map<String, BaseCheck> rules;
	private final Map<String, Check> checks;
	// private final List<String> checks = Lists.newArrayList();
	// private final long lastModified;
	private final static Set<String> logicOperators = Sets.newHashSet();

	static {
		logicOperators.add("and");
		logicOperators.add("or");
		logicOperators.add("not");
	}


	public Enforcer(String policyPath) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		File file = new File(policyPath);
		// lastModified = file.lastModified();
		// URL url = getClass().getResource(policyPath);
		// File file = new File(url.toURI());
		// URL url = Cms.class.getResource(policyPath);
		this.rules = Maps.newHashMap();
		Map<String, String> rules = JsonUtils.readJson(file);
		logger.debug("policy map size:{}, {}", new Object[] { rules.size(), rules });
		checks = Maps.newHashMap();
		setChecks(checks);
		setRules(rules);

	}

	private void setChecks(Map<String, Check> checks) {
		checks.put("rule", new RuleCheck());
		checks.put("role", new RoleCheck());
		checks.put("http", new HttpCheck());
		checks.put("none", new GenericCheck());

	}

	// load_rules load_json
	public void setRules(Map<String, String> rules) {
		// this.rules = rules;
		for (Entry<String, String> entry : rules.entrySet()) {
			logger.debug("put rules key:{}, value:{} ", new Object[] { entry.getKey(), entry.getValue() });
			this.getRules().put(entry.getKey(), parseRule(entry.getValue()));
		}
	}

	// parse_rule _parse_text_rule
	private BaseCheck parseRule(String value) {
		if (Strings.isNullOrEmpty(value)) {
			return new TrueCheck();
		}

		ParseState state = new ParseState();
		List<Entry<String, BaseCheck>> entrys = parseTokenize(value);
		// logger.debug("list entry begin");
		// for (Entry<String, BaseCheck> entry : entrys) {
		// logger.debug("entry key:{} , rule:{}", new Object[] { entry.getKey(),
		// entry.getValue().getRule() });
		// if (entry.getValue() instanceof Check) {
		// Check b = (Check) entry.getValue();
		// logger.debug("kind:{}, match:{}", new Object[] { b.getKind(),
		// b.getMatch() });
		// }
		// }
		// logger.debug("list entry end");

		for (Entry<String, BaseCheck> entry : entrys) {
			state.shift(entry);
		}

		try {
			logger.debug("parse rule: {} --> {}", new Object[] { value, state.getResult().getValue().getRule() });
			return state.getResult().getValue();
		} catch (IllegalArgumentException e) {
			logger.error(FAIL_TO_UNDERSTAND_RULE, value);
			logger.error("", e);
			return new FalseCheck();
		}
	}

	private List<Entry<String, BaseCheck>> parseTokenize(String orig) {
		String[] tokens = orig.split("\\s+");

		List<Entry<String, BaseCheck>> entrys = Lists.newArrayList();

		for (String token : tokens) {
			if (Strings.isNullOrEmpty(token)) {
				continue;
			}
			String clean = lstrip(token, "(");
			int range = token.length() - clean.length();
			// logger.debug("lstrip: {}, range:{}", new Object[] { clean, range
			// });
			for (int i = 0; i < range; i++) {
				BaseCheck check = new StringCheck("(");
				entrys.add(Maps.immutableEntry("(", check));
			}

			if (Strings.isNullOrEmpty(clean)) {
				continue;
			} else {
				token = clean;
			}

			clean = rstrip(token, ")");
			int trail = token.length() - clean.length();
			// logger.debug("rstrip: {}, trail:{}", new Object[] { clean, trail
			// });
			String lowered = clean.toLowerCase();
			if (logicOperators.contains(lowered)) {
				BaseCheck check = new StringCheck(clean);
				entrys.add(Maps.immutableEntry(lowered, check));
			} else if (!Strings.isNullOrEmpty(clean)) {
				if (token.length() >= 2
						&& ((token.charAt(0) == '\"' && token.charAt(token.length()) == '\"') || (token.charAt(0) == '\'' && token
								.charAt(token.length()) == '\''))) {
					BaseCheck check = new StringCheck(token.substring(1, token.length() - 1));
					entrys.add(Maps.immutableEntry("string", check));
				} else {
					entrys.add(Maps.immutableEntry("check", parseCheck(clean)));
				}
			}

			for (int i = 0; i < trail; i++) {
				BaseCheck check = new StringCheck(")");
				entrys.add(Maps.immutableEntry(")", check));
			}
		}

		return entrys;
	}

	private BaseCheck parseCheck(String clean) {
		if (clean.equals("|")) {
			return new FalseCheck();
		} else if (clean.equals("@")) {
			return new TrueCheck();
		}

		String[] splitStr;
		try {
			splitStr = clean.split(":", 2);
		} catch (Exception e) {
			logger.error(FAIL_TO_UNDERSTAND_RULE, clean);
			return new FalseCheck();
		}
		String kind = splitStr[0];
		String match = splitStr[1];

		if (checks.containsKey(kind)) {
			Check check = checks.get(kind).newInstance(kind, match);
			return check;
		} else if (checks.containsKey(NONE)) {
			Check check = checks.get(NONE).newInstance(kind, match);
			return check;
		} else {
			logger.error(NO_HANDLER, splitStr[0]);
			return new FalseCheck();
		}
	}

	// rule, target, creds
	public boolean enforce(Token token, String action, Map<String, PolicyEntity> target, Map<String, Object> parMap,
			boolean doRaise) {
		logger.debug(RULE_ENFORCED, action);
		// action ,target,creds soRaise,

		boolean result = false;
		if (Strings.isNullOrEmpty(action)) {
			result = false;
		} else {
			if (rules.containsKey(action)) {
				BaseCheck check = this.rules.get(action);
				result = check.check(target, token, parMap, this);
			} else {
				logger.debug(RULE_DOES_NOT_EXIST, action);
				result = false;
			}
		}

		if (doRaise && !result) {
			throw Exceptions.PolicyNotAuthorizedException.getInstance(null, action);
		}
		return result;
	}

	private static String lstrip(String orig, String strip) {
		char[] origs = orig.toCharArray();
		char[] tokens = strip.toCharArray();
		StringBuilder ret = new StringBuilder();
		boolean isStriped = false;

		for (int i = 0; i < origs.length; i++) {
			boolean isEquals = false;
			for (int j = 0; j < tokens.length; j++) {
				if (tokens[j] == origs[i]) {
					isEquals = true;
				}
			}
			if (isStriped) {
				ret.append(origs[i]);
				continue;
			} else {
				if (isEquals) {
					continue;
				} else {
					isStriped = true;
					ret.append(origs[i]);
				}
			}
		}
		return ret.toString();
	}

	private static String rstrip(String orig, String strip) {
		char[] origs = orig.toCharArray();
		char[] tokens = strip.toCharArray();
		StringBuilder ret = new StringBuilder();
		boolean isStriped = false;

		for (int i = origs.length - 1; i >= 0; i--) {
			boolean isEquals = false;
			for (int j = 0; j < tokens.length; j++) {
				if (tokens[j] == origs[i]) {
					isEquals = true;
				}
			}
			if (isStriped) {
				ret.append(origs[i]);
				continue;
			} else {
				if (isEquals) {
					continue;
				} else {
					isStriped = true;
					ret.append(origs[i]);
				}
			}
		}
		return ret.reverse().toString();
	}

	public Map<String, BaseCheck> getRules() {
		return rules;
	}
}
