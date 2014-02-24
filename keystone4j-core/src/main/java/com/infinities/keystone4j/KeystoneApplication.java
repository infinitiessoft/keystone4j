package com.infinities.keystone4j;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

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
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.api.CredentialApiFactory;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.credential.controller.impl.CredentialV3ControllerFactory;
import com.infinities.keystone4j.credential.driver.CredentialDriverFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.IdentityApiFactory;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.controller.impl.GroupV3ControllerFactory;
import com.infinities.keystone4j.identity.controller.impl.UserV3ControllerFactory;
import com.infinities.keystone4j.identity.driver.IdentityDriverFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.api.PolicyApiFactory;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.policy.controller.impl.PolicyV3ControllerFactory;
import com.infinities.keystone4j.policy.driver.PolicyDriverFactory;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.api.TokenApiFactory;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.api.TokenProviderApiFactory;
import com.infinities.keystone4j.token.provider.driver.TokenProviderDriverFactory;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.api.TrustApiFactory;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;
import com.infinities.keystone4j.trust.controller.impl.TrustV3ControllerFactory;
import com.infinities.keystone4j.trust.driver.TrustDriverFactory;

public class KeystoneApplication extends ResourceConfig {

	public KeystoneApplication() {
		register(new AbstractBinder() {

			@Override
			protected void configure() {
				// assignment
				bindFactory(AssignmentApiFactory.class).to(AssignmentApi.class);
				bindFactory(DomainV3ControllerFactory.class).to(DomainV3Controller.class);
				bindFactory(ProjectV3ControllerFactory.class).to(ProjectV3Controller.class);
				bindFactory(RoleAssignmentV3ControllerFactory.class).to(RoleAssignmentV3Controller.class);
				bindFactory(RoleV3ControllerFactory.class).to(RoleV3Controller.class);
				bindFactory(AssignmentDriverFactory.class).to(AssignmentDriver.class);

				// auth
				bindFactory(AuthControllerFactory.class).to(AuthController.class);

				// catalog
				bindFactory(CatalogApiFactory.class).to(CatalogApi.class);
				bindFactory(EndpointV3ControllerFactory.class).to(EndpointV3Controller.class);
				bindFactory(ServiceV3ControllerFactory.class).to(ServiceV3Controller.class);
				bindFactory(CatalogDriverFactory.class).to(CatalogDriver.class);

				// credential
				bindFactory(CredentialApiFactory.class).to(CredentialApi.class);
				bindFactory(CredentialV3ControllerFactory.class).to(CredentialV3Controller.class);
				bindFactory(CredentialDriverFactory.class).to(CredentialDriver.class);

				// identity
				bindFactory(IdentityApiFactory.class).to(IdentityApi.class);
				bindFactory(GroupV3ControllerFactory.class).to(GroupV3Controller.class);
				bindFactory(UserV3ControllerFactory.class).to(UserV3Controller.class);
				bindFactory(IdentityDriverFactory.class).to(IdentityDriver.class);

				// policy
				bindFactory(PolicyApiFactory.class).to(PolicyApi.class);
				bindFactory(PolicyV3ControllerFactory.class).to(PolicyV3Controller.class);
				bindFactory(PolicyDriverFactory.class).to(PolicyDriver.class);

				// token
				bindFactory(TokenApiFactory.class).to(TokenApi.class);
				bindFactory(TokenProviderApiFactory.class).to(TokenProviderApi.class);
				bindFactory(TokenProviderDriverFactory.class).to(TokenProviderDriver.class);

				// trust
				bindFactory(TrustApiFactory.class).to(TrustApi.class);
				bindFactory(TrustV3ControllerFactory.class).to(TrustV3Controller.class);
				bindFactory(TrustDriverFactory.class).to(TrustDriver.class);
			}

		});

	}
}
