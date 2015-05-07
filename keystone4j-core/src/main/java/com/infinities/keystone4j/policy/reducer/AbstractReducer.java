/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.policy.reducer;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.policy.BaseReducer;
import com.infinities.keystone4j.policy.check.BaseCheck;

public abstract class AbstractReducer implements BaseReducer {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractReducer.class);
	private final BaseReducer reducer;


	public AbstractReducer(BaseReducer reducer) {
		this.reducer = reducer;
	}

	@Override
	// reduce
	public Entry<String, BaseCheck> reduce(List<Entry<String, BaseCheck>> entrys) {
		// logger.debug("{} get entrys: {}", this.getClass().getSimpleName(),
		// entrys.size());
		for (List<String> reducer : getReducers()) {
			List<String> keys = Lists.newArrayList();
			for (Entry<String, BaseCheck> entry : entrys) {
				keys.add(entry.getKey());
			}
			// logger.debug("keys: {}", keys);

			int start = entrys.size() - reducer.size();
			int end = entrys.size();
			if (entrys.size() >= reducer.size() && keys.subList(start, end).equals(reducer)) {
				// logger.debug("{} match, remove {} ~ {}", new Object[] {
				// this.getClass().getSimpleName(), start, end });
				Entry<String, BaseCheck> newEntry = getEntry(entrys.subList(start, end));
				// logger.debug("new entrys size: {}", entrys.size());
				for (int i = start; i < end; i++) {
					entrys.remove(start);
				}

				entrys.add(newEntry);
				// logger.debug("new entrys size: {}", entrys.size());
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
