package com.infinities.keystone4j.api.command.decorator;

import java.util.List;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.common.Hints;

public class ResponseTruncatedCommand<T> implements TruncatedCommand<T> {

	private final TruncatedCommand<T> command;
	private final Driver driver;


	public ResponseTruncatedCommand(TruncatedCommand<T> command, Driver driver) {
		this.command = command;
		this.driver = driver;
	}

	@Override
	public List<T> execute(Hints hints) throws Exception {
		if (hints != null) {
			return command.execute(hints);
		}

		Integer listLimit = driver.getListLimit();
		if (listLimit != null && listLimit > 0) {
			hints = new Hints();
			hints.setLimit(listLimit);
		}
		return command.execute(hints);
	}

}
