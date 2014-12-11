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
