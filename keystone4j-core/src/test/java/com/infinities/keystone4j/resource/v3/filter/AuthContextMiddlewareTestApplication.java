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
//package com.infinities.keystone4j.resource.v3.filter;
//
//import javax.inject.Singleton;
//
//import org.glassfish.hk2.utilities.binding.AbstractBinder;
//import org.glassfish.jersey.server.ResourceConfig;
//
//import com.infinities.keystone4j.common.api.VersionApi;
//import com.infinities.keystone4j.common.api.VersionApiFactory;
//import com.infinities.keystone4j.filter.AuthContextMiddleware;
//import com.infinities.keystone4j.main.AdminResource;
//import com.infinities.keystone4j.mock.MockTokenApiFactory;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.utils.jackson.JacksonFeature;
//
//public class AuthContextMiddlewareTestApplication extends ResourceConfig {
//
//	public AuthContextMiddlewareTestApplication(TokenApi tokenApi) {
//		final MockTokenApiFactory tokenApiFactory = new MockTokenApiFactory(tokenApi);
//		register(JacksonFeature.class);
//		register(new AbstractBinder() {
//
//			@Override
//			protected void configure() {
//
//				// // assignment
//				// bindFactory(AssignmentApiFactory.class).to(AssignmentApi.class);
//				// bindFactory(DomainV3ControllerFactory.class).to(DomainV3Controller.class);
//				// bindFactory(ProjectV3ControllerFactory.class).to(ProjectV3Controller.class);
//				// bindFactory(RoleAssignmentV3ControllerFactory.class).to(RoleAssignmentV3Controller.class);
//				// bindFactory(RoleV3ControllerFactory.class).to(RoleV3Controller.class);
//				// bindFactory(AssignmentDriverFactory.class).to(AssignmentDriver.class);
//				//
//				// // auth
//				// bindFactory(AuthControllerFactory.class).to(AuthController.class);
//				//
//				// // catalog
//				// bindFactory(CatalogApiFactory.class).to(CatalogApi.class);
//				// bindFactory(EndpointV3ControllerFactory.class).to(EndpointV3Controller.class);
//				// bindFactory(ServiceV3ControllerFactory.class).to(ServiceV3Controller.class);
//				// bindFactory(CatalogDriverFactory.class).to(CatalogDriver.class);
//				//
//				// // credential
//				// bindFactory(CredentialApiFactory.class).to(CredentialApi.class);
//				// bindFactory(CredentialV3ControllerFactory.class).to(CredentialV3Controller.class);
//				// bindFactory(CredentialDriverFactory.class).to(CredentialDriver.class);
//				//
//				// // identity
//				// bindFactory(IdentityApiFactory.class).to(IdentityApi.class);
//				// bindFactory(GroupV3ControllerFactory.class).to(GroupV3Controller.class);
//				// bindFactory(UserV3ControllerFactory.class).to(UserV3Controller.class);
//				// bindFactory(IdentityDriverFactory.class).to(IdentityDriver.class);
//				//
//				// // policy
//				// bindFactory(PolicyApiFactory.class).to(PolicyApi.class);
//				// bindFactory(PolicyV3ControllerFactory.class).to(PolicyV3Controller.class);
//				// bindFactory(PolicyDriverFactory.class).to(PolicyDriver.class);
//				//
//				// // token
//				// bindFactory(TokenApiFactory.class).to(TokenApi.class);
//				// bindFactory(TokenProviderApiFactory.class).to(TokenProviderApi.class);
//				// bindFactory(TokenProviderDriverFactory.class).to(TokenProviderDriver.class);
//				//
//				// // trust
//				// bindFactory(TrustApiFactory.class).to(TrustApi.class);
//				// bindFactory(TrustV3ControllerFactory.class).to(TrustV3Controller.class);
//				// bindFactory(TrustDriverFactory.class).to(TrustDriver.class);
//				//
//				// // endpoint_pointer
//				// bindFactory(EndpointFilterApiFactory.class).to(EndpointFilterApi.class);
//				// bindFactory(EndpointFilterControllerFactory.class).to(EndpointFilterController.class);
//				// bindFactory(EndpointFilterDriverFactory.class).to(EndpointFilterDriver.class);
//				//
//				// // extension
//				// bindFactory(ExtensionApiFactory.class).to(ExtensionApi.class);
//				//
//				// // simple_cert
//				// bindFactory(SimpleCertV3ControllerFactory.class).to(SimpleCertV3Controller.class);
//				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
//				bindFactory(tokenApiFactory).to(TokenApi.class);
//			}
//
//		});
//		register(AuthContextMiddleware.class);
//		// register(TokenAuthMiddleware.class);
//		// register(AdminTokenAuthMiddleware.class);
//		// register(RequestBodySizeLimiter.class);
//		register(AdminResource.class);
//
//	}
// }
