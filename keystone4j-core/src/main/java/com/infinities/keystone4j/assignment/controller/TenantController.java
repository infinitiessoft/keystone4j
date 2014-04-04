package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.assignment.TenantWrapper;
import com.infinities.keystone4j.model.assignment.TenantsWrapper;
import com.infinities.keystone4j.model.identity.UsersWrapper;

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
