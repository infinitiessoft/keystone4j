package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.assignment.model.TenantWrapper;
import com.infinities.keystone4j.assignment.model.TenantsWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;

public interface TenantController {

	TenantsWrapper getAllProjects();

	TenantsWrapper getProjectsForToken();

	TenantWrapper getProject(String tenantid);

	TenantWrapper getProjectByName();

	TenantWrapper createProject();

	TenantWrapper updateProject();

	void deleteProject();

	UsersWrapper getProjectUsers();

}
