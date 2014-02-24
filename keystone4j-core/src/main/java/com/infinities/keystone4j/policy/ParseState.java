package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.policy.reducer.ExtendAndExprReducer;
import com.infinities.keystone4j.policy.reducer.ExtendOrExprReducer;
import com.infinities.keystone4j.policy.reducer.MakeAndExprReducer;
import com.infinities.keystone4j.policy.reducer.MakeNotExprReducer;
import com.infinities.keystone4j.policy.reducer.MakeOrExprReducer;
import com.infinities.keystone4j.policy.reducer.WrapCheckReducer;

public class ParseState {

	private final static String CLOUD_NOT_PARSE = "Could not parse rule";
	private final List<Entry<String, BaseCheck>> entrys;
	private final BaseReducer reducer = new WrapCheckReducer(new MakeAndExprReducer(new ExtendAndExprReducer(
			new MakeOrExprReducer(new ExtendOrExprReducer(new MakeNotExprReducer(null))))));


	public ParseState() {
		entrys = Lists.newArrayList();
	}

	public void reduce() {
		while (reducer.reduce(entrys) != null)
			;
	}

	public void shift(Entry<String, BaseCheck> entry) {
		entrys.add(entry);
		this.reduce();
	}

	public Entry<String, BaseCheck> getResult() {
		if (entrys.size() != 1) {
			// ValueError
			throw new IllegalStateException(CLOUD_NOT_PARSE);
		}
		return entrys.get(0);
	}

}
