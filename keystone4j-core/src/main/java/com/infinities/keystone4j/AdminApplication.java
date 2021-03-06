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
package com.infinities.keystone4j;

import java.io.File;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.infinities.keystone4j.admin.v3.AccountAdminResource;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.AssignmentApiFactory;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.controller.impl.DomainV3ControllerFactory;
import com.infinities.keystone4j.assignment.controller.impl.ProjectV3ControllerFactory;
import com.infinities.keystone4j.assignment.controller.impl.RoleAssignmentV3ControllerFactory;
import com.infinities.keystone4j.assignment.controller.impl.RoleV3ControllerFactory;
import com.infinities.keystone4j.assignment.driver.AssignmentDriverFactory;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.controller.impl.AuthControllerFactory;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.CatalogApiFactory;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.catalog.controller.impl.EndpointV3ControllerFactory;
import com.infinities.keystone4j.catalog.controller.impl.ServiceV3ControllerFactory;
import com.infinities.keystone4j.catalog.driver.CatalogDriverFactory;
import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;
import com.infinities.keystone4j.cert.controller.impl.SimpleCertV3ControllerFactory;
import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.api.VersionApiFactory;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.contrib.revoke.driver.RevokeDriver;
import com.infinities.keystone4j.contrib.revoke.driver.impl.RevokeDriverFactory;
import com.infinities.keystone4j.contrib.revoke.impl.RevokeApiFactory;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.api.CredentialApiFactory;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.credential.controller.impl.CredentialV3ControllerFactory;
import com.infinities.keystone4j.credential.driver.CredentialDriverFactory;
import com.infinities.keystone4j.extension.ExtensionApi;
import com.infinities.keystone4j.extension.ExtensionApiFactory;
import com.infinities.keystone4j.filter.AdminTokenAuthMiddleware;
import com.infinities.keystone4j.filter.AuthContextMiddleware;
import com.infinities.keystone4j.filter.TokenAuthMiddleware;
import com.infinities.keystone4j.identity.IdGenerator;
import com.infinities.keystone4j.identity.IdGeneratorApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.MappingDriver;
import com.infinities.keystone4j.identity.api.IdGeneratorApiFactory;
import com.infinities.keystone4j.identity.api.IdMappingApiFactory;
import com.infinities.keystone4j.identity.api.IdentityApiFactory;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.controller.impl.GroupV3ControllerFactory;
import com.infinities.keystone4j.identity.controller.impl.UserV3ControllerFactory;
import com.infinities.keystone4j.identity.driver.IdentityDriverFactory;
import com.infinities.keystone4j.identity.driver.mapping.IdMappingDriverFactory;
import com.infinities.keystone4j.identity.id_generators.Sha256IdGeneratorFactory;
import com.infinities.keystone4j.jpa.EntityManagerInterceptor;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.api.PolicyApiFactory;
import com.infinities.keystone4j.policy.driver.PolicyDriverFactory;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.controller.TokenController;
import com.infinities.keystone4j.token.controller.impl.TokenV2ControllerFactory;
import com.infinities.keystone4j.token.driver.TokenDriverFactory;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.persistence.manager.PersistenceManagerFactory;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.api.TokenProviderApiFactory;
import com.infinities.keystone4j.token.provider.driver.TokenProviderDriverFactory;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.api.TrustApiFactory;
import com.infinities.keystone4j.trust.driver.TrustDriverFactory;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;

public class AdminApplication extends ResourceConfig {

	public static String CONF_DIR = "conf" + File.separator;


	public AdminApplication() {
		this.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bindFactory(CredentialV3ControllerFactory.class).to(CredentialV3Controller.class);
				bindFactory(DomainV3ControllerFactory.class).to(DomainV3Controller.class);
				bindFactory(RoleV3ControllerFactory.class).to(RoleV3Controller.class);
				bindFactory(EndpointV3ControllerFactory.class).to(EndpointV3Controller.class);
				bindFactory(UserV3ControllerFactory.class).to(UserV3Controller.class);
				bindFactory(GroupV3ControllerFactory.class).to(GroupV3Controller.class);
				bindFactory(ProjectV3ControllerFactory.class).to(ProjectV3Controller.class);
				bindFactory(RoleAssignmentV3ControllerFactory.class).to(RoleAssignmentV3Controller.class);
				bindFactory(ServiceV3ControllerFactory.class).to(ServiceV3Controller.class);
				bindFactory(SimpleCertV3ControllerFactory.class).to(SimpleCertV3Controller.class);
				bindFactory(ExtensionApiFactory.class).to(ExtensionApi.class);

				// // assignment
				bindFactory(AssignmentApiFactory.class).to(AssignmentApi.class);
				bindFactory(AssignmentDriverFactory.class).to(AssignmentDriver.class);
				// auth
				bindFactory(AuthControllerFactory.class).to(AuthController.class);
				// catalog
				bindFactory(CatalogApiFactory.class).to(CatalogApi.class);
				bindFactory(CatalogDriverFactory.class).to(CatalogDriver.class);
				// credential
				bindFactory(CredentialApiFactory.class).to(CredentialApi.class);
				bindFactory(CredentialDriverFactory.class).to(CredentialDriver.class);
				// identity
				bindFactory(IdentityApiFactory.class).to(IdentityApi.class);
				bindFactory(IdentityDriverFactory.class).to(IdentityDriver.class);
				bindFactory(IdMappingApiFactory.class).to(IdMappingApi.class);
				bindFactory(IdMappingDriverFactory.class).to(MappingDriver.class);
				bindFactory(IdGeneratorApiFactory.class).to(IdGeneratorApi.class);
				bindFactory(Sha256IdGeneratorFactory.class).to(IdGenerator.class);
				// policy
				bindFactory(PolicyApiFactory.class).to(PolicyApi.class);
				bindFactory(PolicyDriverFactory.class).to(PolicyDriver.class);
				// token
				bindFactory(TokenProviderApiFactory.class).to(TokenProviderApi.class);
				bindFactory(TokenProviderDriverFactory.class).to(TokenProviderDriver.class);
				bindFactory(PersistenceManagerFactory.class).to(PersistenceManager.class);
				bindFactory(TokenDriverFactory.class).to(TokenDriver.class);
				bindFactory(TokenV2ControllerFactory.class).to(TokenController.class);
				// trust
				bindFactory(TrustApiFactory.class).to(TrustApi.class);
				bindFactory(TrustDriverFactory.class).to(TrustDriver.class);
				// version
				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
				// revoke
				bindFactory(RevokeApiFactory.class).to(RevokeApi.class);
				bindFactory(RevokeDriverFactory.class).to(RevokeDriver.class);
			}

		});
		this.register(GenericExceptionMapper.class);
		this.register(CORSResponseFilter.class);
		this.register(EntityManagerInterceptor.class);
		this.register(AuthContextMiddleware.class);
		this.register(TokenAuthMiddleware.class);
		this.register(AdminTokenAuthMiddleware.class);
		// this.register(RequestBodySizeLimiter.class);
		this.register(AccountAdminResource.class);
		// this.register(ObjectMapperResolver.class);
		this.register(JacksonFeature.class);
	}
}
