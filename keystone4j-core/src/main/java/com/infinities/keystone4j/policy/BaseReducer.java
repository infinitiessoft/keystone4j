package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map.Entry;

import com.infinities.keystone4j.policy.check.BaseCheck;

public interface BaseReducer {

	public List<List<String>> getReducers();

	public Entry<String, BaseCheck> reduce(List<Entry<String, BaseCheck>> entrys);

}
