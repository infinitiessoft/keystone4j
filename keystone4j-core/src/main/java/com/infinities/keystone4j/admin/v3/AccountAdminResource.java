/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.wrapper.UserWrapper;
import com.infinities.keystone4j.model.utils.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/admin")
public class AccountAdminResource {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AccountAdminResource.class);
	private final UserV3Controller userController;


	@Inject
	public AccountAdminResource(UserV3Controller userController) throws Exception {
		this.userController = userController;
	}

	@POST
	@Path("/users/{userid}/enable")
	@JsonView(Views.Advance.class)
	public UserWrapper enableUser(@PathParam("userid") String userid) throws Exception {
		// add vm
		User user = new User();
		user.setEnabled(true);
		UserWrapper ret = (UserWrapper) userController.updateUser(userid, user);
		// send mail
		return ret;
	}

	@POST
	@Path("/users/{userid}/disable")
	@JsonView(Views.Advance.class)
	public UserWrapper disableUser(@PathParam("userid") String userid) throws Exception {
		User user = new User();
		user.setEnabled(false);
		return (UserWrapper) userController.updateUser(userid, user);
	}

	@Path("/accounts")
	public Class<AccountResource> getRegisterResource() {
		return AccountResource.class;
	}

	@Path("/users")
	// assignment
	public Class<UserV3Resource> getUserV3Resource() {
		return UserV3Resource.class;
	}

	@Path("/projects")
	// assignment
	public Class<ProjectResource> getProjectResource() {
		return ProjectResource.class;
	}

}
