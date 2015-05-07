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
package com.infinities.keystone4j.catalog.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.ServiceDao;
import com.infinities.keystone4j.model.catalog.Service;

public class ListServicesFunction implements ListFunction<Service> {

	@Override
	public List<Service> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new ServiceDao().listService(hints);
	}

}
