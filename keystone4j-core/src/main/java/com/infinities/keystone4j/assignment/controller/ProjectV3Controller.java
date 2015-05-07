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
package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;

public interface ProjectV3Controller {

	MemberWrapper<Project> createProject(Project project) throws Exception;

	CollectionWrapper<Project> listProjects() throws Exception;

	CollectionWrapper<Project> listUserProjects(String userid) throws Exception;

	MemberWrapper<Project> getProject(String projectid) throws Exception;

	MemberWrapper<Project> updateProject(String projectid, Project project) throws Exception;

	void deleteProject(String projectid) throws Exception;

	// CollectionWrapper<Project> getProjectUsers(String projectid, Boolean
	// enabled, String name, int page, int perPage)
	// throws Exception;

}
