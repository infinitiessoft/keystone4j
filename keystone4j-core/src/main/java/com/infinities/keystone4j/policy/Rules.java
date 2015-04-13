package com.infinities.keystone4j.policy;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.policy.check.BaseCheck;
import com.infinities.keystone4j.policy.check.Check;
import com.infinities.keystone4j.policy.check.FalseCheck;
import com.infinities.keystone4j.policy.check.GenericCheck;
import com.infinities.keystone4j.policy.check.HttpCheck;
import com.infinities.keystone4j.policy.check.RoleCheck;
import com.infinities.keystone4j.policy.check.RuleCheck;
import com.infinities.keystone4j.policy.check.StringCheck;
import com.infinities.keystone4j.policy.check.TrueCheck;
import com.infinities.keystone4j.utils.JsonUtils;

public class Rules implements Map<String, BaseCheck> {

	private final static Logger logger = LoggerFactory.getLogger(Rules.class);
	private final static String NO_HANDLER = "No handler for matches of kind {}";
	private final static String FAIL_TO_UNDERSTAND_RULE = "Failed to understand rule {}";
	private final static String NONE = "none";
	private Map<String, BaseCheck> rules;
	private final Object defaultRule;
	private final static Map<String, Check> checks = Maps.newHashMap();
	static {
		checks.put("rule", new RuleCheck());
		checks.put("role", new RoleCheck());
		checks.put("http", new HttpCheck());
		checks.put("none", new GenericCheck());
	}
	// private final List<String> checks = Lists.newArrayList();
	// private final long lastModified;
	private final static Set<String> logicOperators = Sets.newHashSet();

	static {
		logicOperators.add("and");
		logicOperators.add("or");
		logicOperators.add("not");
	}


	public Rules(Map<String, BaseCheck> rules, Object defaultRule) {
		this.rules = rules;
		if (this.rules == null) {
			this.rules = Maps.newHashMap();
		}
		this.defaultRule = defaultRule;
	}

	public static Rules loadJson(String data, Object defaultRule) throws IOException {
		Map<String, String> map = JsonUtils.readJson(data, new TypeReference<LinkedHashMap<String, String>>() {
		});

		Map<String, BaseCheck> rules = Maps.newHashMap();
		for (Entry<String, String> entry : map.entrySet()) {
			logger.debug("put rules key:{}, value:{} ", new Object[] { entry.getKey(), entry.getValue() });
			rules.put(entry.getKey(), parseRule(entry.getValue()));
		}

		logger.debug("defaultRule: {}", defaultRule);
		return new Rules(rules, defaultRule);
	}

	// parse_rule _parse_text_rule
	private static BaseCheck parseRule(String rule) {
		if (Strings.isNullOrEmpty(rule)) {
			return new TrueCheck();
		}

		ParseState state = new ParseState();
		List<Entry<String, BaseCheck>> entrys = parseTokenize(rule);

		for (Entry<String, BaseCheck> entry : entrys) {
			state.shift(entry);
		}

		try {
			logger.debug("parse rule: {} --> {}", new Object[] { rule, state.getResult().getValue().getRule() });
			return state.getResult().getValue();
		} catch (IllegalArgumentException e) {
			logger.error(FAIL_TO_UNDERSTAND_RULE, rule);
			logger.error("", e);
			return new FalseCheck();
		}
	}

	private static List<Entry<String, BaseCheck>> parseTokenize(String rule) {
		String[] tokens = rule.split("\\s+");

		List<Entry<String, BaseCheck>> entrys = Lists.newArrayList();

		for (String token : tokens) {
			token = token.trim();
			if (Strings.isNullOrEmpty(token)) {
				continue;
			}
			String clean = StringUtils.removeStart(token, "(");
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

			clean = StringUtils.removeEnd(token, ")");
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

	private static BaseCheck parseCheck(String rule) {
		if (rule.equals("|")) {
			return new FalseCheck();
		} else if (rule.equals("@")) {
			return new TrueCheck();
		}

		String[] splitStr;
		try {
			splitStr = rule.split(":", 2);
		} catch (Exception e) {
			logger.error(FAIL_TO_UNDERSTAND_RULE, rule);
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
			logger.error(NO_HANDLER, kind);
			return new FalseCheck();
		}
	}

	// private static String lstrip(String orig, String strip) {
	// char[] origs = orig.toCharArray();
	// char[] tokens = strip.toCharArray();
	// StringBuilder ret = new StringBuilder();
	// boolean isStriped = false;
	//
	// for (int i = 0; i < origs.length; i++) {
	// boolean isEquals = false;
	// for (int j = 0; j < tokens.length; j++) {
	// if (tokens[j] == origs[i]) {
	// isEquals = true;
	// }
	// }
	// if (isStriped) {
	// ret.append(origs[i]);
	// continue;
	// } else {
	// if (isEquals) {
	// continue;
	// } else {
	// isStriped = true;
	// ret.append(origs[i]);
	// }
	// }
	// }
	// return ret.toString();
	// }
	//
	// private static String rstrip(String orig, String strip) {
	// char[] origs = orig.toCharArray();
	// char[] tokens = strip.toCharArray();
	// StringBuilder ret = new StringBuilder();
	// boolean isStriped = false;
	//
	// for (int i = origs.length - 1; i >= 0; i--) {
	// boolean isEquals = false;
	// for (int j = 0; j < tokens.length; j++) {
	// if (tokens[j] == origs[i]) {
	// isEquals = true;
	// }
	// }
	// if (isStriped) {
	// ret.append(origs[i]);
	// continue;
	// } else {
	// if (isEquals) {
	// continue;
	// } else {
	// isStriped = true;
	// ret.append(origs[i]);
	// }
	// }
	// }
	// return ret.reverse().toString();
	// }

	public Map<String, BaseCheck> getRules() {
		return rules;
	}

	@Override
	public void clear() {
		rules.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return rules.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return rules.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, BaseCheck>> entrySet() {
		return rules.entrySet();
	}

	@Override
	public BaseCheck get(Object key) {
		BaseCheck value = rules.get(key);

		if (value != null) {
			return value;
		}

		if (defaultRule == null) {
			throw new IllegalArgumentException((String) key);
		}

		if (defaultRule instanceof BaseCheck) {
			return (BaseCheck) defaultRule;
		}

		if (defaultRule instanceof String) {
			if (rules.containsKey(defaultRule)) {
				return rules.get(defaultRule);
			} else {
				throw new IllegalArgumentException((String) key);
			}
		}

		throw new IllegalArgumentException((String) key);
	}

	@Override
	public boolean isEmpty() {
		return rules.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return rules.keySet();
	}

	@Override
	public BaseCheck put(String key, BaseCheck value) {
		return rules.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends BaseCheck> m) {
		rules.putAll(m);
	}

	@Override
	public BaseCheck remove(Object key) {
		return rules.remove(key);
	}

	@Override
	public int size() {
		return rules.size();
	}

	@Override
	public Collection<BaseCheck> values() {
		return rules.values();
	}

}