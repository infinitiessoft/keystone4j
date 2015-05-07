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
package com.infinities.keystone4j.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ListFunction;

public class TruncatedFunction<T> implements ListFunction<T> {

	private final static Logger logger = LoggerFactory.getLogger(TruncatedFunction.class);
	private final ListFunction<T> innerFunction;


	public TruncatedFunction(ListFunction<T> innerFunction) {
		this.innerFunction = innerFunction;
	}

	@Override
	public List<T> execute(Hints hints) throws Exception {
		if (hints.getLimit() == null) {
			return innerFunction.execute(hints);
		}
		int listLimit = hints.getLimit().getLimit();
		hints.setLimit(listLimit + 1);
		List<T> refList = innerFunction.execute(hints);

		if (refList.size() > listLimit) {
			logger.debug("refs size: {}, limit: {}", new Object[] { refList.size(), listLimit });
			hints.setLimit(listLimit, true);
			return refList.subList(0, listLimit);
		} else {
			hints.setLimit(listLimit);
			return refList;
		}

	}

}
