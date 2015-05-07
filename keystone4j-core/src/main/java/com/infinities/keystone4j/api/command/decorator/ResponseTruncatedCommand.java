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
