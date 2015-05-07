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
package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.policy.check.BaseCheck;
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
	private final static Logger logger = LoggerFactory.getLogger(ParseState.class);


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
			for (Entry<String, BaseCheck> entry : entrys) {
				logger.debug("value error:{}, {}", new Object[] { entry.getKey(), entry.getValue().getRule() });

			}

			throw new IllegalStateException(CLOUD_NOT_PARSE);
		}
		return entrys.get(0);
	}
}
