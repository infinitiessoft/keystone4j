package com.infinities.keystone4j.api;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.api.VersionApiFactory;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.mock.MockAssignmentApiFactory;
import com.infinities.keystone4j.mock.MockAssignmentDriverFactory;
import com.infinities.keystone4j.mock.MockCredentialApiFactory;
import com.infinities.keystone4j.mock.MockIdentityApiFactory;
import com.infinities.keystone4j.mock.MockTokenApiFactory;
import com.infinities.keystone4j.token.TokenApi;

public class ApiTestApplication extends ResourceConfig {

	public ApiTestApplication(CredentialApi credentialApi, TokenApi tokenApi, AssignmentApi assignmentApi,
			IdentityApi identityApi, AssignmentDriver driver) {
		final MockCredentialApiFactory credentialApiFactory = new MockCredentialApiFactory(credentialApi);
		final MockTokenApiFactory tokenApiFactory = new MockTokenApiFactory(tokenApi);
		final MockIdentityApiFactory identityApiFactory = new MockIdentityApiFactory(identityApi);
		final MockAssignmentApiFactory assignmentApiFactory = new MockAssignmentApiFactory(assignmentApi);
		final MockAssignmentDriverFactory assignmentDriverFactory = new MockAssignmentDriverFactory(driver);

		this.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
				bindFactory(tokenApiFactory).to(TokenApi.class);
				bindFactory(assignmentApiFactory).to(AssignmentApi.class);
				bindFactory(identityApiFactory).to(IdentityApi.class);
				bindFactory(credentialApiFactory).to(CredentialApi.class);
				bindFactory(assignmentDriverFactory).to(AssignmentDriver.class);
			}

		});
	}
}
