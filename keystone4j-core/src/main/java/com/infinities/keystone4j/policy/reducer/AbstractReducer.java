package com.infinities.keystone4j.policy.reducer;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.BaseReducer;

public abstract class AbstractReducer implements BaseReducer {

	private final BaseReducer reducer;


	public AbstractReducer(BaseReducer reducer) {
		this.reducer = reducer;
	}

	@Override
	// reduce
	public Entry<String, BaseCheck> reduce(List<Entry<String, BaseCheck>> entrys) {
		for (List<String> reducer : getReducers()) {
			List<String> keys = Lists.newArrayList();
			for (Entry<String, BaseCheck> entry : entrys) {
				keys.add(entry.getKey());
			}

			int start = entrys.size() - reducer.size();
			int end = entrys.size() - 1;
			if (entrys.size() >= reducer.size() && keys.subList(start, end).retainAll(reducer)) {
				Entry<String, BaseCheck> newEntry = getEntry(entrys.subList(start, end));
				entrys.add(newEntry);
				for (int i = start; i < end; i++) {
					entrys.remove(i);
				}
				return newEntry;
			}
		}

		if (reducer == null) {
			return null;
		}

		return reducer.reduce(entrys);
	}

	protected abstract Entry<String, BaseCheck> getEntry(List<Entry<String, BaseCheck>> entry);
}
