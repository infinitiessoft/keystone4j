package com.infinities.keystone4j.policy.reducer;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.BaseReducer;
import com.infinities.keystone4j.policy.check.OrCheck;

public class MakeOrExprReducer extends AbstractReducer {

	public MakeOrExprReducer(BaseReducer reducer) {
		super(reducer);
	}


	private final static List<List<String>> reducers = Lists.newArrayList();
	static {
		List<String> reducer1 = Lists.newArrayList();
		reducer1.add("check");
		reducer1.add("or");
		reducer1.add("check");
		reducers.add(reducer1);
	}


	@Override
	public List<List<String>> getReducers() {
		return reducers;
	}

	@Override
	public Entry<String, BaseCheck> getEntry(List<Entry<String, BaseCheck>> entry) {
		List<BaseCheck> checks = Lists.newArrayList();
		checks.add(entry.get(0).getValue());
		checks.add(entry.get(2).getValue());
		BaseCheck orCheck = new OrCheck(checks);
		return Maps.immutableEntry("or_expr", orCheck);
	}
}
