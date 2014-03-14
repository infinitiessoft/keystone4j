package com.infinities.keystone4j.main;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.api.VersionApiFactory;

public class PublicResourceTestApplication extends ResourceConfig {

	public PublicResourceTestApplication() {
		register(JacksonFeature.class);
		register(new AbstractBinder() {

			@Override
			protected void configure() {

				// // assignment
				// bindFactory(AssignmentApiFactory.class).to(AssignmentApi.class);
				// bindFactory(DomainV3ControllerFactory.class).to(DomainV3Controller.class);
				// bindFactory(ProjectV3ControllerFactory.class).to(ProjectV3Controller.class);
				// bindFactory(RoleAssignmentV3ControllerFactory.class).to(RoleAssignmentV3Controller.class);
				// bindFactory(RoleV3ControllerFactory.class).to(RoleV3Controller.class);
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

				// versionApi
				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
			}

		});
		// register(AuthContextMiddleware.class);
		// register(TokenAuthMiddleware.class);
		// register(AdminTokenAuthMiddleware.class);
		// register(RequestBodySizeLimiter.class);
		register(PublicResource.class);
	}
}
