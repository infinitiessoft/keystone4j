package com.infinities.keystone4j.admin.v3;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.infinities.keystone4j.admin.AdminResource;
import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.api.VersionApiFactory;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;

public class ApiV3ResourceTestApplication extends ResourceConfig {

	public ApiV3ResourceTestApplication() {
		register(JacksonFeature.class);
		register(new AbstractBinder() {

			@Override
			protected void configure() {
				// versionApi
				bindFactory(VersionApiFactory.class).to(VersionApi.class).in(Singleton.class);
			}

		});
		register(AdminResource.class);
	}
}
