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
