package com.infinities.keystone4j.resource.v3.project;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.controller.impl.ProjectV3ControllerFactory;
import com.infinities.keystone4j.assignment.controller.impl.RoleV3ControllerFactory;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.api.VersionApiFactory;
import com.infinities.keystone4j.filter.AdminTokenAuthMiddleware;
import com.infinities.keystone4j.filter.AuthContextMiddleware;
import com.infinities.keystone4j.filter.RequestBodySizeLimiter;
import com.infinities.keystone4j.filter.TokenAuthMiddleware;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.main.PublicResource;
import com.infinities.keystone4j.mock.MockAssignmentApiFactory;
import com.infinities.keystone4j.mock.MockCatalogApiFactory;
import com.infinities.keystone4j.mock.MockIdentityApiFactory;
import com.infinities.keystone4j.mock.MockPolicyApiFactory;
import com.infinities.keystone4j.mock.MockTokenApiFactory;
import com.infinities.keystone4j.mock.MockTokenProviderApiFactory;
import com.infinities.keystone4j.mock.MockTrustApiFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class ProjectResourceTestApplication extends ResourceConfig {

	public ProjectResourceTestApplication(CatalogApi catalogApi, TokenApi tokenApi, TokenProviderApi tokenProviderApi,
			AssignmentApi assignmentApi, IdentityApi identityApi, PolicyApi policyApi, TrustApi trustApi) {
		final MockCatalogApiFactory catalogApiFactory = new MockCatalogApiFactory(catalogApi);
		final MockTokenApiFactory tokenApiFactory = new MockTokenApiFactory(tokenApi);
		final MockTokenProviderApiFactory tokenProviderApiFactory = new MockTokenProviderApiFactory(tokenProviderApi);
		final MockIdentityApiFactory identityApiFactory = new MockIdentityApiFactory(identityApi);
		final MockAssignmentApiFactory assignmentApiFactory = new MockAssignmentApiFactory(assignmentApi);
		final MockPolicyApiFactory policyApiFactory = new MockPolicyApiFactory(policyApi);
		final MockTrustApiFactory trustApiFactory = new MockTrustApiFactory(trustApi);

		this.register(new AbstractBinder() {

			@Override
			protected void configure() {

				// // assignment
				// bindFactory(AssignmentApiFactory.class).to(AssignmentApi.class);
				// bindFactory(DomainV3ControllerFactory.class).to(DomainV3Controller.class);
				bindFactory(ProjectV3ControllerFactory.class).to(ProjectV3Controller.class);
				// bindFactory(RoleAssignmentV3ControllerFactory.class).to(RoleAssignmentV3Controller.class);
				bindFactory(RoleV3ControllerFactory.class).to(RoleV3Controller.class);
				// bindFactory(AssignmentDriverFactory.class).to(AssignmentDriver.class);
				//
				// // auth
				// bindFactory(AuthControllerFactory.class).to(AuthController.class);
				//
				// // catalog
				// bindFactory(CatalogApiFactory.class).to(CatalogApi.class);
				// bindFactory(EndpointV3ControllerFactory.class).to(EndpointV3Controller.class);
				// bindFactory(ServiceV3ControllerFactory.class).to(ServiceV3Controller.class);
				// bindFactory(CatalogDriverFactory.class).to(CatalogDriver.class);
				//
				// // credential
				// bindFactory(CredentialApiFactory.class).to(CredentialApi.class);
				// bindFactory(CredentialV3ControllerFactory.class).to(CredentialV3Controller.class);
				// bindFactory(CredentialDriverFactory.class).to(CredentialDriver.class);
				//
				// // identity
				// bindFactory(IdentityApiFactory.class).to(IdentityApi.class);
				// bindFactory(GroupV3ControllerFactory.class).to(GroupV3Controller.class);
				// bindFactory(UserV3ControllerFactory.class).to(UserV3Controller.class);
				// bindFactory(IdentityDriverFactory.class).to(IdentityDriver.class);
				//
				// // policy
				// bindFactory(PolicyApiFactory.class).to(PolicyApi.class);
				// bindFactory(PolicyV3ControllerFactory.class).to(PolicyV3Controller.class);
				// bindFactory(PolicyDriverFactory.class).to(PolicyDriver.class);
				//
				// // token
				// bindFactory(TokenApiFactory.class).to(TokenApi.class);
				// bindFactory(TokenProviderApiFactory.class).to(TokenProviderApi.class);
				// bindFactory(TokenProviderDriverFactory.class).to(TokenProviderDriver.class);
				//
				// // trust
				// bindFactory(TrustApiFactory.class).to(TrustApi.class);
				// bindFactory(TrustV3ControllerFactory.class).to(TrustV3Controller.class);
				// bindFactory(TrustDriverFactory.class).to(TrustDriver.class);
				//
				// // endpoint_pointer
				// bindFactory(EndpointFilterApiFactory.class).to(EndpointFilterApi.class);
				// bindFactory(EndpointFilterControllerFactory.class).to(EndpointFilterController.class);
				// bindFactory(EndpointFilterDriverFactory.class).to(EndpointFilterDriver.class);
				//
				// // extension
				// bindFactory(ExtensionApiFactory.class).to(ExtensionApi.class);
				//
				// // simple_cert
				// bindFactory(SimpleCertV3ControllerFactory.class).to(SimpleCertV3Controller.class);
				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
				bindFactory(tokenApiFactory).to(TokenApi.class);
				bindFactory(tokenProviderApiFactory).to(TokenProviderApi.class);
				bindFactory(assignmentApiFactory).to(AssignmentApi.class);
				bindFactory(identityApiFactory).to(IdentityApi.class);
				bindFactory(policyApiFactory).to(PolicyApi.class);
				bindFactory(trustApiFactory).to(TrustApi.class);
				bindFactory(catalogApiFactory).to(CatalogApi.class);
			}

		});
		this.register(AuthContextMiddleware.class);
		this.register(TokenAuthMiddleware.class);
		this.register(AdminTokenAuthMiddleware.class);
		this.register(RequestBodySizeLimiter.class);
		this.register(PublicResource.class);
		this.register(ObjectMapperResolver.class);
		this.register(JacksonFeature.class);
	}
}
