package com.infinities.keystone4j.policy.reducer;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.BaseReducer;
import com.infinities.keystone4j.policy.check.NotCheck;

public class MakeNotExprReducer extends AbstractReducer {

	private final static List<List<String>> reducers = Lists.newArrayList();
	static {
		List<String> reducer1 = Lists.newArrayList();
		reducer1.add("not");
		reducer1.add("check");
		reducers.add(reducer1);
	}


	public MakeNotExprReducer(BaseReducer reducer) {
		super(reducer);
	}

	@Override
	public List<List<String>> getReducers() {
		return reducers;
	}

	@Override
	public Entry<String, BaseCheck> getEntry(List<Entry<String, BaseCheck>> entry) {
		BaseCheck notCheck = new NotCheck(entry.get(1).getValue());
		return Maps.immutableEntry("check", notCheck);
	}
}
