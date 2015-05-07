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
package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;

public class MockAssignmentApiFactory implements Factory<AssignmentApi> {

	private final AssignmentApi assignmentApi;


	public MockAssignmentApiFactory(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public void dispose(AssignmentApi arg0) {

	}

	@Override
	public AssignmentApi provide() {
		return assignmentApi;
	}

}
