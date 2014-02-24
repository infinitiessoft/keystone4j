package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.ProjectWrapper;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;

public interface ProjectV3Controller {

	ProjectWrapper createProject(Project project);

	ProjectsWrapper listProjects(String domainid, String name, Boolean enabled, int page, int perPage);

	ProjectsWrapper listUserProjects(String userid, Boolean enabled, String name, int page, int perPage);

	ProjectWrapper getProject(String projectid);

	ProjectWrapper updateProject(String projectid, Project project);

	void deleteProject(String projectid);

}
