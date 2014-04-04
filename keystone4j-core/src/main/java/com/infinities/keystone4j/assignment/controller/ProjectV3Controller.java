package com.infinities.keystone4j.assignment.controller;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.ProjectWrapper;
import com.infinities.keystone4j.model.assignment.ProjectsWrapper;
import com.infinities.keystone4j.model.identity.User;

public interface ProjectV3Controller {

	ProjectWrapper createProject(Project project);

	ProjectsWrapper listProjects(String domainid, String name, Boolean enabled, int page, int perPage);

	ProjectsWrapper listUserProjects(String userid, Boolean enabled, String name, int page, int perPage);

	ProjectWrapper getProject(String projectid);

	ProjectWrapper updateProject(String projectid, Project project);

	void deleteProject(String projectid);

	List<User> getProjectUsers(String projectid, Boolean enabled, String name, int page, int perPage);

}
